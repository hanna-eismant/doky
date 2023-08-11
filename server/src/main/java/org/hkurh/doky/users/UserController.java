package org.hkurh.doky.users;

import org.hkurh.doky.security.DokyAuthority;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured(DokyAuthority.Role.ROLE_USER)
public class UserController implements UserApi {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    @GetMapping("/users/current")
    public UserDto getUser() {
        return userFacade.getCurrentUser();
    }
}
