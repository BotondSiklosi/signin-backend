package com.botondsiklosi.signin.init;

import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class Init {

    private final UserService userService;

    @Bean
    public CommandLineRunner createUsers() {
        return args -> {
            userService.checkIfAlreadyRegisteredAndRegister(UserCredentials.builder()
                    .username("admin")
                    .email("admin@thegod.com")
                    .password("admin")
                    .build());

        };
    }

}
