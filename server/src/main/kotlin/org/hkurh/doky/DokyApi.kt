package org.hkurh.doky

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "General Information")
interface DokyApi {

    @Operation(summary = "Get information about current app version")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Returns app version information successfully"),
    )
    fun appVersion(): VersionResponse
}
