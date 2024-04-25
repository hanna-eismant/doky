package org.hkurh.doky.search

import org.hkurh.doky.search.solr.DocumentResultBean
import java.util.*

interface DocumentIndexService {
    fun fullIndex()
    fun updateIndex(runDate: Date)
    fun search(query: String): List<DocumentResultBean>
}
