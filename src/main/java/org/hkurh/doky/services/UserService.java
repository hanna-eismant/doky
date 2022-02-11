package org.hkurh.doky.services;


import org.hkurh.doky.entities.UserEntity;
import org.springframework.lang.NonNull;

public interface UserService {

    UserEntity findUserByUid(@NonNull final String userUid);

    boolean checkUserExistence(@NonNull final String userUid);

    UserEntity create(@NonNull final String userUid, @NonNull final String encodedPassword);
}
