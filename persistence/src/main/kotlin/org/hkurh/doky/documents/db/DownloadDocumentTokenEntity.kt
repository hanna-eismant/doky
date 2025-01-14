package org.hkurh.doky.documents.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hkurh.doky.users.db.UserEntity

@Entity
@Table(
    name = "download_document_tokens",
    indexes = [Index(name = "idx_download_document_tokens_token", columnList = "token")],
    uniqueConstraints = [
        UniqueConstraint(name = "uc_download_document_tokens_token", columnNames = ["token"]),
        UniqueConstraint(
            name = "uc_download_document_tokens_app_user_document",
            columnNames = ["app_user", "document"]
        )]
)
class DownloadDocumentTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null

    @OneToOne
    @JoinColumn(name = "app_user", nullable = false, unique = true)
    lateinit var user: UserEntity

    @OneToOne
    @JoinColumn(name = "document", nullable = false, unique = true)
    lateinit var document: DocumentEntity

    @Column(name = "token", nullable = false, unique = true)
    lateinit var token: String
}
