package org.hkurh.doky.documents

import jakarta.persistence.*
import org.hkurh.doky.workflow.WorkflowEntity

@Entity
@Table(name = "document_type")
class DocumentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "code", nullable = false, unique = true)
    var code: String? = null

    @Column(name = "name")
    var name: String? = null

    @Lob
    @Column(name = "description")
    var description: String? = null

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    var documents: List<DocumentEntity> = ArrayList()

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    var workflow: List<WorkflowEntity> = ArrayList()
}
