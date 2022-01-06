package org.hkurh.doky.controllers;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

import java.util.Date;

import org.hkurh.doky.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class LoginController {

    @PostMapping("/login")
    public UserDto login(
            @RequestParam String username,
            @RequestParam String password) {

        String token = getToken(username);

        UserDto user = new UserDto();
        user.setUsername(username);
        user.setToken(token);

        return user;
    }

    private String getToken(final String username) {
        final String token = Jwts.builder()
                .setId("dokyToken")
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact();

        return token;

    }
}
