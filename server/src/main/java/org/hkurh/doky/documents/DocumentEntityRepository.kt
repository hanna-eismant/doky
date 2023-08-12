package org.hkurh.doky.documents

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.NonNull
import java.util.*

interface DocumentEntityRepository : CrudRepository<DocumentEntity?, Long?>, JpaSpecificationExecutor<DocumentEntity?> {
    @Query("select d from DocumentEntity d where d.creator.id = ?1")
    fun findByCreatorId(@NonNull documentId: Long?): List<DocumentEntity?>?

    @Query("select d from DocumentEntity d where d.id = ?1 and d.creator.id = ?2")
    fun findByIdAndCreatorId(@NonNull documentId: Long?, @NonNull creatorId: Long?): Optional<DocumentEntity?>?
}
