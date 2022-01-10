package org.hkurh.doky.facades;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("UserFacade unit test")
class UserFacadeImplUnitTest {

    @Spy
    ModelMapper userModelMapper = MapperFactory.getUserModelMapper();
    @Spy
    @InjectMocks
    private UserFacadeImpl userFacade;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should throw exception when user is incorrect")
    void shouldThrowException_whenUserIsIncorrect() {
        String userUid = "user";
        String userPass = "pass";

        when(userService.checkUser(userUid, userPass)).thenReturn(false);

        DokyAuthenticationException exception = assertThrows(DokyAuthenticationException.class,
                () -> userFacade.loginUser(userUid, userPass),
                "Exception should be thrown when user credentials are incorrect");
    }

    @Test
    @DisplayName("Should set token when user is valid")
    void shouldSetToken_whenUserIsValid() {
        String userUid = "user";
        String userPass = "pass";
        UserEntity userEntity = new UserEntity();

        when(userService.checkUser(userUid, userPass)).thenReturn(true);
        when(userService.findUserByUid(userUid)).thenReturn(userEntity);

        UserDto actualUserDto = userFacade.loginUser(userUid, userPass);
        String actualToken = actualUserDto.getToken();

        assertNotNull(actualToken, "Token cannot be null");
        assertNotEquals(StringUtils.EMPTY, actualToken, "Token cannot be empty");
    }
}
