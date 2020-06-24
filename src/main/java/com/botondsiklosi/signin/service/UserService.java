package com.botondsiklosi.signin.service;

import com.botondsiklosi.signin.entity.User;
import com.botondsiklosi.signin.model.Role;
import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    // true if already registered
    public boolean checkIfAlreadyRegisteredAndRegister(UserCredentials userData) {
        if (userRepository.findByUsername(userData.getUsername()) == null) {
            userRepository.save(User.builder()
                    .username(userData.getUsername())
                    .email(userData.getEmail())
                    .roles( new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN)) )
                    .password(encoder.encode(userData.getPassword()))
                    .build());
            return false;
        }

        return true;
    }

}
