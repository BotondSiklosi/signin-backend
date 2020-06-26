package com.botondsiklosi.signin.controller;

import com.botondsiklosi.signin.model.UserCredentials;
import com.botondsiklosi.signin.repository.MyUserRepository;
import com.botondsiklosi.signin.security.JwtUtil;
import com.botondsiklosi.signin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MyUserRepository myUserRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

//    private final AuthenticationManager authenticationManager;
//
//    private final JwtTokenService jwtTokenService;
//
//    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenService = jwtTokenService;
//    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCredentials userData) {
        if (userService.checkIfAlreadyRegisteredAndRegister(userData)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userData.getUsername());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already registered!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserCredentials userData, HttpServletResponse response){

        System.out.println(userData.getUsername());
        System.out.println(userData.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userData.getUsername(),
                userData.getPassword()
        ));
        String jwtToken = jwtUtil.generateToken(authentication);
        addTokenToCookie(response, jwtToken);
        return ResponseEntity.ok().body(userData.getUsername());

    }

    @GetMapping("/logout")
    public ResponseEntity<?> signOut(HttpServletResponse response) throws AuthenticationException {
        try {
            Cookie cookieToken = new Cookie("token", "loggedOut");
            cookieToken.setMaxAge(0);
            cookieToken.setHttpOnly(true);
            cookieToken.setPath("/");
            response.addCookie(cookieToken);

            return ResponseEntity.ok("");
        } catch (Exception e) {
            throw new AuthenticationException("Logout failed");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        if (SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            return ResponseEntity.status(403).body("No user!");
        }
        return  ResponseEntity.ok(SecurityContextHolder.getContext()
                .getAuthentication().getName());

    }


    private void addTokenToCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .domain("localhost") // should be parameterized
                .sameSite("Strict")  // CSRF
//                .secure(true)
                .maxAge(Duration.ofHours(24))
                .httpOnly(true)      // XSS
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }


}
