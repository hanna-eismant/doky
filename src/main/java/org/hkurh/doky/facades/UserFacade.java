package org.hkurh.doky.facades;

import org.hkurh.doky.dto.UserDto;
import org.springframework.lang.NonNull;

public interface UserFacade {

    UserDto loginUser(@NonNull final String userUid, @NonNull final String password);
}
