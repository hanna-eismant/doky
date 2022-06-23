package org.hkurh.doky.controllers;

import org.hkurh.doky.facades.UserFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    public void shouldLogin() {
        userController.login(USER_UID, USER_PASS);

        verify(userFacade, times(1)).login(USER_UID, USER_PASS);
    }

    @Test
    @DisplayName("Should register")
    public void shouldRegister() {
        userController.register(USER_UID, USER_PASS);

        verify(userFacade, times(1)).register(USER_UID, USER_PASS);
    }
}
