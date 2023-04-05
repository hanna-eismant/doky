package org.hkurh.doky.controllers.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegistrationRequest {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(max = 32, message = "Should has less than 32 characters")
    @Size(min = 8, message = "Should has minimum 8 characters")
    private String password;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
