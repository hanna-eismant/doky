package org.hkurh.doky.search

import org.apache.solr.common.SolrDocument
import org.hkurh.doky.documents.db.DocumentEntity

fun DocumentEntity.toSolrBean(): DocumentBean {
    val entity = this
    return DocumentBean().apply {
        id = entity.id.toString()
        name = entity.name
        description = entity.description
        fileName = entity.fileName
    }
}

fun SolrDocument.toSolrBean(): DocumentBean {
    val solrDoc = this
    return DocumentBean().apply {
        solrDoc["id"]?.let { i -> id = i.toString() }
        solrDoc["name"]?.let { n -> name = n.toString() }
        solrDoc["description"]?.let { d -> description = d.toString() }
    }
}
