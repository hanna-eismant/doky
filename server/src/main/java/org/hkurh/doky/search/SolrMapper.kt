package org.hkurh.doky.search

import org.hkurh.doky.documents.db.DocumentEntity

fun DocumentEntity.toSolrBean(): DocumentBean {
    val entity = this
    val dto = DocumentBean().apply {
        id = entity.id.toString()
        name = entity.name
        description = entity.description
        fileName = entity.fileName
    }
    return dto
}
