package org.hkurh.doky.search

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class DocumentIndexData @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("fileName") val fileName: String?,
    @JsonProperty("createdDate") val createdDate: OffsetDateTime?,
    @JsonProperty("modifiedDate") val modifiedDate: OffsetDateTime?,
    @JsonProperty("createdBy") val createdBy: String?,
    @JsonProperty("modifiedBy") val modifiedBy: String?,
)
