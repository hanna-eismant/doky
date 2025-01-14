package org.hkurh.doky.documents.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository


/**
 * Repository interface for managing [DownloadDocumentTokenEntity] objects.
 * Provides methods for querying and persisting [DownloadDocumentTokenEntity] instances in the database.
 * Inherits functionality from [CrudRepository] for common CRUD operations and [JpaSpecificationExecutor]
 * for specification-based queries.
 */
interface DownloadDocumentTokenEntityRepository : CrudRepository<DownloadDocumentTokenEntity?, Long?>,
    JpaSpecificationExecutor<DownloadDocumentTokenEntity?> {

    fun findByUserAndDocument(user: UserEntity, document: DocumentEntity): DownloadDocumentTokenEntity?

}
