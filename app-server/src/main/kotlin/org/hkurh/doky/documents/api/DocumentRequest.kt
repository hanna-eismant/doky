package org.hkurh.doky.documents.api

import jakarta.validation.constraints.NotBlank

/**
 * Represents a document request.
 *
 * @property name The name of the document.
 * @property description The description of the document.
 */
class DocumentRequest {
    @NotBlank(message = "Document Name can not be blank and should contain at least one non space character")
    var name: String = ""
    var description: String = ""
}
