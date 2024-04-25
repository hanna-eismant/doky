package org.hkurh.doky.documents.api

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
