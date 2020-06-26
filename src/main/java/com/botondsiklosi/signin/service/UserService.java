package com.botondsiklosi.signin.service;

import com.botondsiklosi.signin.entity.MyUser;
import com.botondsiklosi.signin.model.Role;
import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    // true if already registered
    public boolean checkIfAlreadyRegisteredAndRegister(UserCredentials userData) {
        if (!myUserRepository.findByUsername(userData.getUsername()).isPresent()) {
            myUserRepository.save(MyUser.builder()
                    .username(userData.getUsername())
                    .email(userData.getEmail())
                    .roles( new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)) )
                    .password(passwordEncoder.encode(userData.getPassword()))
                    .build());
            return false;
        }

        return true;
    }

}
