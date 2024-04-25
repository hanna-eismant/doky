package org.hkurh.doky.search.solr

import org.apache.solr.client.solrj.beans.Field

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


