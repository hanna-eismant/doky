package org.hkurh.doky.controllers;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.facades.UserFacade;
import org.hkurh.doky.security.DokyAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured(DokyAuthority.Role.ROLE_USER)
public class UserController {
    private UserFacade userFacade;

    @GetMapping("/users/current")
    public UserDto getUser() {
        return getUserFacade().getCurrentUser();
    }

    private UserFacade getUserFacade() {
        return userFacade;
    }

    @Autowired
    public void setUserFacade(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
