package org.hkurh.doky.search

import org.apache.solr.common.SolrDocument
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.search.solr.DocumentIndexBean
import org.hkurh.doky.search.solr.DocumentResultBean

fun DocumentEntity.toSolrIndexBean(): DocumentIndexBean {
    val entity = this
    return DocumentIndexBean().apply {
        id = entity.id.toString()
        name = entity.name
        description = entity.description
        fileName = entity.fileName
    }
}

fun SolrDocument.toSolrResultBean(): DocumentResultBean {
    val solrDoc = this
    return DocumentResultBean().apply {
        solrDoc["id"]?.let { i -> id = i.toString() }
    }
}
