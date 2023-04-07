package org.hkurh.doky.services;


import org.hkurh.doky.entities.UserEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface UserService {

    UserEntity findUserByUid(@NonNull String userUid);

    boolean checkUserExistence(@NonNull String userUid);

    UserEntity create(@NonNull String userUid, @NonNull String encodedPassword);

    @Nullable
    UserEntity getCurrentUser();
}
