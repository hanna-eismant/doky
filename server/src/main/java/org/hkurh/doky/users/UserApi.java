package org.hkurh.doky.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@SecurityRequirement(name = "Bearer Token")
public interface UserApi {

    @Operation(summary = "Get current user info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User information is retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    UserDto getUser();
}
