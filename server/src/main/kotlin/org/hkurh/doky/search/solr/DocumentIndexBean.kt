package org.hkurh.doky.search.solr

import org.apache.solr.client.solrj.beans.Field

/**
 * The [DocumentIndexBean] class represents a bean object used for indexing documents in a search engine.
 *
 * @property id The unique identifier of the document.
 * @property name The name of the document.
 * @property fileName The file name of the document.
 * @property description The description of the document.
 */
class DocumentIndexBean {
    @Field
    var id: String? = null
    @Field
    var name: String? = null
    @Field
    var fileName: String? = null
    @Field
    var description: String? = null
}


