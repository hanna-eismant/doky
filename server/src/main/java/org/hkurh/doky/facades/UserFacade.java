package org.hkurh.doky.facades;

import org.hkurh.doky.dto.UserDto;
import org.springframework.lang.NonNull;


public interface UserFacade {

    void login(@NonNull String userUid, @NonNull String password);

    UserDto register(@NonNull String username, @NonNull String password);

    UserDto getCurrentUser();
}
