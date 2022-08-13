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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest registrationRequest) {
        var registeredUser = getUserFacade().register(registrationRequest.getUsername(), registrationRequest.getPassword());

        var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(registeredUser.getUserUid());

        return ResponseEntity.created(resourceLocation).build();
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
