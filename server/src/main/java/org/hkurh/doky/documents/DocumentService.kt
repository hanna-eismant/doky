package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity

interface DocumentService {
    fun create(name: String, description: String?): DocumentEntity
    fun find(id: String): DocumentEntity?
    fun find(): List<DocumentEntity>
    fun save(document: DocumentEntity)
}
