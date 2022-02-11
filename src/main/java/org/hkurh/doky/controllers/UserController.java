package org.hkurh.doky.controllers;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.facades.UserFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    private UserFacade userFacade;

    @PostMapping("/login")
    public UserDto login(
            @RequestParam final String username,
            @RequestParam final String password) {

        return getUserFacade().login(username, password);
    }

    @PostMapping("/register")
    public UserDto register(
            @RequestParam final String username,
            @RequestParam final String password) {

        return getUserFacade().register(username, password);
    }

    private UserFacade getUserFacade() {
        return userFacade;
    }

    @Resource
    public void setUserFacade(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
