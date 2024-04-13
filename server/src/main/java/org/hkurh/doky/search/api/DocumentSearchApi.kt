package org.hkurh.doky.search.api

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Documents")
@SecurityRequirement(name = "Bearer Token")
interface DocumentSearchApi {
}
