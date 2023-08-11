package org.hkurh.doky.authorization;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hkurh.doky.errorhandling.ErrorResponse;
import org.hkurh.doky.security.AuthenticationRequest;
import org.hkurh.doky.security.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User authorization and registration")
@SecurityRequirement(name = "")
public interface AuthorizationUserApi {
    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is logged in successful",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect username or password",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest);

    @Operation(summary = "User registration")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User is created",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "409", description = "User with same name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest);
}
