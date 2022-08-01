package org.hkurh.doky.facades;

import org.hkurh.doky.dto.UserDto;
import org.springframework.lang.NonNull;


public interface UserFacade {

    UserDto login(@NonNull final String userUid, @NonNull final String password);

    UserDto register(@NonNull String username, @NonNull String password);
}
