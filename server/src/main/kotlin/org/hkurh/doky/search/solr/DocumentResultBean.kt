package org.hkurh.doky.search.solr

import org.apache.solr.client.solrj.beans.Field

/**
 * The [DocumentResultBean] class represents a bean object used for storing search results of documents.
 *
 * @property id The unique identifier of the document.
 */
class DocumentResultBean {
    @Field
    var id: String? = null
}
