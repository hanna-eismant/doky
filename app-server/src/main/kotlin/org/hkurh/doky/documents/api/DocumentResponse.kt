package org.hkurh.doky.documents.api

/**
 * Represents a document response.
 *
 * @property id The ID of the document.
 * @property name The name of the document.
 * @property description The description of the document.
 * @property fileName The name of the file attached to the document.
 * @property createdBy The username of the user who created the document.
 * @property createdDate The date when the document was created.
 * @property modifiedBy The username of the user who last modified the document.
 * @property modifiedDate The date of the last modification of the document.
 */
class DocumentResponse {
    var id: Long = 0
    var name: String? = null
    var description: String? = null
    var fileName: String? = null
    lateinit var createdBy: String
    lateinit var createdDate: String
    lateinit var modifiedBy: String
    lateinit var modifiedDate: String
}
