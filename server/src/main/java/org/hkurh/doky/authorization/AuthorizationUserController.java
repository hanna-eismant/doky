package org.hkurh.doky.authorization;

import org.hkurh.doky.security.AuthenticationRequest;
import org.hkurh.doky.security.AuthenticationResponse;
import org.hkurh.doky.security.JwtProvider;
import org.hkurh.doky.users.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
public class AuthorizationUserController implements AuthorizationUserApi {
    private UserFacade userFacade;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();
        getUserFacade().checkCredentials(username, password);
        var token = JwtProvider.generateToken(username);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @Override
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        var registeredUser = getUserFacade().register(registrationRequest.getUsername(), registrationRequest.getPassword());
        var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(registeredUser.getUserUid());

        return ResponseEntity.created(resourceLocation).build();
    }

    private UserFacade getUserFacade() {
        return userFacade;
    }

    @Autowired
    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
