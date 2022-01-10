package org.hkurh.doky.facades;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

import java.util.Date;

import javax.annotation.Resource;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.services.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserFacadeImpl implements UserFacade {

    private UserService userService;

    @Override
    public UserDto loginUser(@NonNull final String userUid, @NonNull String password) {
        if (!getUserService().checkUser(userUid, password)) {
            throw new DokyAuthenticationException("Incorrect credentials");
        }

        UserEntity userEntity = getUserService().findUserByUid(userUid);
        UserDto userDto = MapperFactory.getUserModelMapper().map(userEntity, UserDto.class);
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

    public UserService getUserService() {
        return userService;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
