package org.hkurh.doky.workflow

import jakarta.persistence.*
import org.hkurh.doky.documents.db.DocumentTypeEntity

@Entity
@Table(name = "workflow")
class WorkflowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Column(name = "code", nullable = false, unique = true)
    var code: String? = null

    @Column(name = "name")
    var name: String? = null

    @Lob
    @Column(name = "description")
    var description: String? = null

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    var documentType: DocumentTypeEntity? = null
}
