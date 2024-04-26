package org.hkurh.doky.documents.api

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

/**
 * Represents a document request.
 *
 * @property name The name of the document.
 * @property description The description of the document.
 */
class DocumentRequest {
    @NotNull
    @NotBlank
    @NotEmpty
    var name: String = ""
    var description: String = ""
}
