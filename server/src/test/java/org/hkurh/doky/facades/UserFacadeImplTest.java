package org.hkurh.doky.facades;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.exceptions.DokyRegistrationException;
import org.hkurh.doky.facades.impl.UserFacadeImpl;
import org.hkurh.doky.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("UserFacade unit test")
class UserFacadeImplTest {

    private static final String USER_UID = "user";
    private static final String USER_PASS = "pass";
    private static final String USER_PASS_ENCODED = "passEncoded";

    private final UserEntity userEntity = new UserEntity();

    @Spy
    private ModelMapper userModelMapper = MapperFactory.getModelMapper();
    @Spy
    @InjectMocks
    private UserFacadeImpl userFacade;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userEntity.setPassword(USER_PASS_ENCODED);
        userEntity.setUid(USER_UID);
    }

    @Test
    @DisplayName("Should throw exception when user login is incorrect when login")
    void shouldThrowException_whenUserIsIncorrect() {
        when(userService.checkUserExistence(USER_UID)).thenReturn(false);

        final DokyAuthenticationException exception = assertThrows(DokyAuthenticationException.class,
                () -> userFacade.checkCredentials(USER_UID, USER_PASS),
                "Exception should be thrown when user credentials are incorrect");

        assertNotNull(exception, "Exception annot be null");
        assertNotEquals(StringUtils.EMPTY, exception.getMessage(), "Exception should contain message");

    }

    @Test
    @DisplayName("Should done without errors when user is valid when login")
    void shouldSetToken_whenUserIsValid() {
        when(userService.checkUserExistence(USER_UID)).thenReturn(true);
        when(userService.findUserByUid(USER_UID)).thenReturn(userEntity);
        when(passwordEncoder.matches(USER_PASS, USER_PASS_ENCODED)).thenReturn(true);

        userFacade.checkCredentials(USER_UID, USER_PASS);
    }

    @Test
    @DisplayName("Should throw exception when user exists when register")
    void shouldThrowException_whenExistingUserRegister() {
        when(userService.checkUserExistence(USER_UID)).thenReturn(true);

        final DokyRegistrationException exception = assertThrows(DokyRegistrationException.class,
                () -> userFacade.register(USER_UID, USER_PASS),
                "Should throw exception when try to register user with existing uid");

        assertNotNull(exception, "Exception annot be null");
        assertNotEquals(StringUtils.EMPTY, exception.getMessage(), "Exception should contain message");
    }

    @Test
    @DisplayName("Should register user when it doses not exists")
    void shouldRegisterUser_whenUserDoesNotExists() {
        when(passwordEncoder.encode(USER_PASS)).thenReturn(USER_PASS_ENCODED);
        when(userService.create(USER_UID, USER_PASS_ENCODED)).thenReturn(userEntity);

        final UserDto registeredUserDto = userFacade.register(USER_UID, USER_PASS);

        assertNotNull(registeredUserDto, "Registered user cannot be null");
        assertEquals(USER_UID, registeredUserDto.getUserUid(), "User should be registered with provided uid");
    }
}
