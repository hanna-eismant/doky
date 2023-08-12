package org.hkurh.doky.documents

import jakarta.persistence.*
import org.hkurh.doky.users.UserEntity

@Entity
@Table(name = "document")
class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "name")
    var name: String? = null

    @Lob
    @Column(name = "description")
    var description: String? = null

    @Column(name = "file_path")
    var filePath: String? = null

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    var documentType: DocumentTypeEntity? = null

    @ManyToOne
    @JoinColumn(name = "creator_id")
    var creator: UserEntity? = null
}
