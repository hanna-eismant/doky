package org.hkurh.doky.documents.api

/**
 * Represents the response containing a token used for authorizing a file download.
 *
 * @property token The token string used to authorize the download operation.
 */
class DownloadTokenResponse(val token: String = "")
