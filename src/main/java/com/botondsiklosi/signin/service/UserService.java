package com.botondsiklosi.signin.service;

import com.botondsiklosi.signin.entity.User;
import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // true if already registered
    public boolean checkIfAlreadyRegisteredAndRegister(UserCredentials userData) {
        if (userRepository.findByUsername(userData.getUsername()) == null) {
            userRepository.save(new User(userData.getUsername(),userData.getEmail(), userData.getPassword()));
            return false;
        }

        return true;
    }

}
