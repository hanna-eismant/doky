package org.hkurh.doky.documents

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface DocumentEntityRepository : CrudRepository<DocumentEntity?, Long?>, JpaSpecificationExecutor<DocumentEntity?> {
    @Query("select d from DocumentEntity d where d.creator.id = ?1")
    fun findByCreatorId(documentId: Long): List<DocumentEntity?>

    @Query("select d from DocumentEntity d where d.id = ?1 and d.creator.id = ?2")
    fun findByIdAndCreatorId(documentId: Long, creatorId: Long): DocumentEntity?
}
