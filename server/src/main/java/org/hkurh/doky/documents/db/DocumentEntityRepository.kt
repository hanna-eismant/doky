package org.hkurh.doky.documents.db

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface DocumentEntityRepository : CrudRepository<DocumentEntity?, Long?>, JpaSpecificationExecutor<DocumentEntity?> {

    @Query("select d from DocumentEntity d where d.creator.id = ?1")
    fun findByCreatorId(documentId: Long): List<DocumentEntity>

    @Query("select d from DocumentEntity d where d.id = ?1 and d.creator.id = ?2")
    fun findByIdAndCreatorId(documentId: Long, creatorId: Long): DocumentEntity?

    @Query("select d from DocumentEntity d where d.modifiedDate >= ?1")
    fun findLatestModified(modifiedDate: Date): List<DocumentEntity>
}
