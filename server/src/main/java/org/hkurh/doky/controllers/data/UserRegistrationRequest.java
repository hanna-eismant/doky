package org.hkurh.doky.controllers.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 32, message = "Length should be from 4 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d_\\-]*$")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    private String password;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
