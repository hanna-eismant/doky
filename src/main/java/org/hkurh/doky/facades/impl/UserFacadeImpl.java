package org.hkurh.doky.facades.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.exceptions.DokyRegistrationException;
import org.hkurh.doky.facades.MapperFactory;
import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.services.UserService;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;


@Service
public class UserFacadeImpl implements UserFacade {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Resource
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto login(@NonNull final String userUid, @NonNull final String password) {
        if (!getUserService().checkUserExistence(userUid)) {
            throw new DokyAuthenticationException("User doesn't exist");
        }

        final UserEntity userEntity = getUserService().findUserByUid(userUid);

        final String encodedPassword = userEntity.getPassword();
        if (!getPasswordEncoder().matches(password, encodedPassword)) {
            throw new DokyAuthenticationException("Incorrect credentials");
        }

        final UserDto userDto = MapperFactory.getUserModelMapper().map(userEntity, UserDto.class);
        userDto.setToken(generateToken(userDto.getName()));

        return userDto;
    }

    @Override
    public UserDto register(@NonNull final String userUid, @NonNull final String password) {
        if (getUserService().checkUserExistence(userUid)) {
            throw new DokyRegistrationException("User already exists");
        }

        final String encodedPassword = getPasswordEncoder().encode(password);
        final UserEntity userEntity = getUserService().create(userUid, encodedPassword);

        final UserDto userDto = MapperFactory.getUserModelMapper().map(userEntity, UserDto.class);
        userDto.setToken(generateToken(userDto.getName()));

        return userDto;
    }

    private String generateToken(@NonNull final String username) {
        final String token = Jwts.builder()
                .setId("dokyToken")
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    private UserService getUserService() {
        return userService;
    }

    @Resource
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
