package org.hkurh.doky.controllers;

import org.hkurh.doky.controllers.data.UserRegistrationRequest;
import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.security.AuthenticationRequest;
import org.hkurh.doky.security.AuthenticationResponse;
import org.hkurh.doky.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class AuthorizationUserController implements AuthorizationUserApi {
    private UserFacade userFacade;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final AuthenticationRequest authenticationRequest) {
        final var username = authenticationRequest.getUsername();
        final var password = authenticationRequest.getPassword();

        getUserFacade().login(username, password);
        final var token = JwtProvider.generateToken(username);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody final UserRegistrationRequest registrationRequest) {
        final var registeredUser = getUserFacade().register(registrationRequest.getUsername(), registrationRequest.getPassword());

        final var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(registeredUser.getUserUid());

        return ResponseEntity.created(resourceLocation).build();
    }

    private UserFacade getUserFacade() {
        return userFacade;
    }

    @Autowired
    public void setUserFacade(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
