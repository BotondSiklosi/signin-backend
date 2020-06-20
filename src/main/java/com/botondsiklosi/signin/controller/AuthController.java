package com.botondsiklosi.signin.controller;

import com.botondsiklosi.signin.entity.User;
import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.repository.UserRepository;
import com.botondsiklosi.signin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

//    private final AuthenticationManager authenticationManager;
//
//    private final JwtTokenService jwtTokenService;
//
//    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenService = jwtTokenService;
//    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserCredentials userData, HttpServletResponse response) throws AuthenticationException {
        try {
            if (!userService.checkIfAlreadyRegisteredAndRegister(userData)) return ResponseEntity.ok("");
            throw new Exception();
        } catch (Exception e) {
            throw new AuthenticationException("register failed");
        }
    }


    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody UserCredentials userData, HttpServletResponse response) throws AuthenticationException{

        try {
            User user = userRepository.findByUsername(userData.getUsername());
            Cookie cookieToken = new Cookie("token", "signedIn");
            cookieToken.setMaxAge(60 * 60 * 24);
            cookieToken.setHttpOnly(true);
            cookieToken.setPath("/");
            response.addCookie(cookieToken);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username/password");
        }

    }

    @GetMapping("/signOut")
    public ResponseEntity signOut(HttpServletResponse response) throws AuthenticationException {
        try {
            Cookie cookieToken = new Cookie("token", "signedIn");
            cookieToken.setMaxAge(0);
            cookieToken.setHttpOnly(true);
            cookieToken.setPath("/");
            response.addCookie(cookieToken);

            return ResponseEntity.ok("");
        } catch (Exception e) {
            throw new AuthenticationException("Logout failed");
        }
    }




}
