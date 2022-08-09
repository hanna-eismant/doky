package org.hkurh.doky.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.security.AuthenticationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController unit test")
public class UserControllerTest {
    private static final String USER_UID = "user";
    private static final String USER_PASS = "pass";

    @InjectMocks
    private UserController userController;
    @Mock
    private UserFacade userFacade;

    @Test
    @DisplayName("Should login")
    void shouldLogin() {
        var request = new AuthenticationRequest();
        request.setUsername(USER_UID);
        request.setPassword(USER_PASS);

        userController.login(request);

        verify(userFacade, times(1)).login(USER_UID, USER_PASS);
    }

    @Test
    @DisplayName("Should register")
    void shouldRegister() {
        userController.register(USER_UID, USER_PASS);

        verify(userFacade, times(1)).register(USER_UID, USER_PASS);
    }
}
