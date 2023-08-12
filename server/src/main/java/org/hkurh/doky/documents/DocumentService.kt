package org.hkurh.doky.documents

import org.springframework.lang.NonNull
import java.util.*

interface DocumentService {
    fun create(@NonNull name: String?, description: String?): DocumentEntity?
    fun find(@NonNull id: String?): Optional<DocumentEntity?>?

    @NonNull
    fun find(): List<DocumentEntity?>?
    fun save(@NonNull document: DocumentEntity?)
}
