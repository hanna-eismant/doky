package org.hkurh.doky.search

import org.apache.solr.client.solrj.beans.Field

class DocumentBean {
    @Field
    var id: String? = null
    @Field
    var name: String? = null
    @Field
    var fileName: String? = null
    @Field
    var description: String? = null
}
