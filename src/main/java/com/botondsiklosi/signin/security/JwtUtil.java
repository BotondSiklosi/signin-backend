package com.botondsiklosi.signin.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtUtil {

    @Value("${jwt.expiration.minutes}")
    private int jwtExpirationMinutes;

    private Key secretKey;

    @PostConstruct
    private void initKey() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public UsernamePasswordAuthenticationToken validateTokenAndExtractUserSpringToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        ArrayList<String> rolesList = claims.get("roles", ArrayList.class);
        List<SimpleGrantedAuthority> roles =
                rolesList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null, roles);
    }

    public String generateToken(Authentication authentication) {
        List<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * jwtExpirationMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
