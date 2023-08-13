package org.hkurh.doky.documents

interface DocumentService {
    fun create(name: String, description: String?): DocumentEntity
    fun find(id: String): DocumentEntity?
    fun find(): List<DocumentEntity?>
    fun save(document: DocumentEntity)
}
