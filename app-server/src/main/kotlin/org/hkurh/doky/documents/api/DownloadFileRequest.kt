package org.hkurh.doky.documents.api

import jakarta.validation.constraints.NotBlank

/**
 * Represents a request to download a file.
 *
 * @property token The token used to authorize the file download. This field cannot be blank.
 */
class DownloadFileRequest {
    @NotBlank(message = "Token can not be blank")
    var token: String = ""
}
