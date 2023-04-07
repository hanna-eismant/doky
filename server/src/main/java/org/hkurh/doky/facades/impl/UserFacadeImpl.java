package org.hkurh.doky.facades.impl;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.exceptions.DokyRegistrationException;
import org.hkurh.doky.facades.MapperFactory;
import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Override
    public void checkCredentials(@NonNull final String userUid, @NonNull final String password) {
        if (!getUserService().checkUserExistence(userUid)) {
            throw new DokyAuthenticationException("User doesn't exist");
        }
        final UserEntity userEntity = getUserService().findUserByUid(userUid);
        final String encodedPassword = userEntity.getPassword();
        if (!getPasswordEncoder().matches(password, encodedPassword)) {
            throw new DokyAuthenticationException("Incorrect credentials");
        }
    }

    @Override
    public UserDto register(@NonNull final String userUid, @NonNull final String password) {
        if (getUserService().checkUserExistence(userUid)) {
            throw new DokyRegistrationException("User already exists");
        }
        final String encodedPassword = getPasswordEncoder().encode(password);
        final UserEntity userEntity = getUserService().create(userUid, encodedPassword);
        LOG.info(format("Register new user [%s]", userEntity));
        return MapperFactory.getModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getCurrentUser() {
        final UserEntity userEntity = getUserService().getCurrentUser();
        return MapperFactory.getModelMapper().map(userEntity, UserDto.class);
    }

    private UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
