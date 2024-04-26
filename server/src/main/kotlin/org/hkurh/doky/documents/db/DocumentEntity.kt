package org.hkurh.doky.documents.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*


@Entity
@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "document")
class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    var createdDate: Date? = null

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: UserEntity? = null

    @Column(name = "modified_date")
    @LastModifiedDate
    var modifiedDate: Date? = null

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "modified_by")
    var modifiedBy: UserEntity? = null

    @Column(name = "name")
    var name: String? = null

    @Lob
    @Column(name = "description")
    var description: String? = null

    @Column(name = "file_path")
    var filePath: String? = null

    @Column(name = "file_name")
    var fileName: String? = null

    @ManyToOne
    @JoinColumn(name = "creator_id")
    var creator: UserEntity? = null
}
