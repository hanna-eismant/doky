package org.hkurh.doky.search

import java.time.OffsetDateTime

data class DocumentIndexData(
    val id: String,
    val name: String?,
    val description: String?,
    val fileName: String?,
    val createdDate: OffsetDateTime?,
    val modifiedDate: OffsetDateTime?,
    val createdBy: String?,
    val modifiedBy: String?
)
