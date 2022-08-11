package org.hkurh.doky.controllers;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.security.AuthenticationRequest;
import org.hkurh.doky.security.AuthenticationResponse;
import org.hkurh.doky.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserFacade userFacade;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();

        getUserFacade().login(username, password);
        var token = JwtProvider.generateToken(username);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegistrationRequest registrationRequest) {
        return getUserFacade().register(registrationRequest.getUsername(), registrationRequest.getPassword());
    }

    @GetMapping("/users/current")
    public UserDto getUser() {
        return getUserFacade().getCurrentUser();
    }

    private UserFacade getUserFacade() {
        return userFacade;
    }

    @Autowired
    public void setUserFacade(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
