package org.hkurh.doky.users;


import org.springframework.lang.NonNull;

public interface UserService {

    UserEntity findUserByUid(@NonNull String userUid);

    boolean checkUserExistence(@NonNull String userUid);

    UserEntity create(@NonNull String userUid, @NonNull String encodedPassword);

    @NonNull
    UserEntity getCurrentUser();
}
