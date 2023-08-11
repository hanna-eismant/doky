package org.hkurh.doky.users;

import org.hkurh.doky.errorhandling.DokyAuthenticationException;
import org.hkurh.doky.errorhandling.DokyRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.hkurh.doky.MapperFactory.getModelMapper;

@Component
public class UserFacadeImpl implements UserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(UserFacadeImpl.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserFacadeImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void checkCredentials(@NonNull final String userUid, @NonNull final String password) {
        if (!userService.checkUserExistence(userUid)) {
            throw new DokyAuthenticationException("User doesn't exist");
        }
        final UserEntity userEntity = userService.findUserByUid(userUid);
        final String encodedPassword = userEntity.getPassword();
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new DokyAuthenticationException("Incorrect credentials");
        }
    }

    @Override
    public UserDto register(@NonNull final String userUid, @NonNull final String password) {
        if (userService.checkUserExistence(userUid)) {
            throw new DokyRegistrationException("User already exists");
        }
        final String encodedPassword = passwordEncoder.encode(password);
        final UserEntity userEntity = userService.create(userUid, encodedPassword);
        LOG.info(format("Register new user [%s]", userEntity));
        return getModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getCurrentUser() {
        final UserEntity userEntity = userService.getCurrentUser();
        return getModelMapper().map(userEntity, UserDto.class);
    }
}
