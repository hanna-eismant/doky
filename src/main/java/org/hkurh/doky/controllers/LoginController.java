package org.hkurh.doky.controllers;

import javax.annotation.Resource;

import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.facades.UserFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private UserFacade userFacade;

    @PostMapping("/login")
    public UserDto login(
            @RequestParam String username,
            @RequestParam String password) {

        UserDto userDto = getUserFacade().loginUser(username, password);
        return userDto;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    @Resource
    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
