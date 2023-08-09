package org.hkurh.doky.users;

import org.hkurh.doky.errorhandling.DokyAuthenticationException;
import org.springframework.lang.NonNull;


public interface UserFacade {

    /**
     * Check if provided credentials are correct.
     *
     * @throws DokyAuthenticationException if user with provided username does not exist,
     *                                     or provided password is incorrect
     */
    void checkCredentials(@NonNull String userUid, @NonNull String password);

    UserDto register(@NonNull String username, @NonNull String password);

    UserDto getCurrentUser();
}
