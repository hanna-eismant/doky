package org.hkurh.doky.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {

    @Schema(description = "Unique username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 32, message = "Length should be from 4 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d_\\-]*$")
    private String username;
    @Schema(description = "User password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
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
