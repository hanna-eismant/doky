package org.hkurh.doky.documents;

import jakarta.persistence.*;
import org.hkurh.doky.workflow.WorkflowEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "document_type")
public class DocumentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    private List<DocumentEntity> documents = new ArrayList<>();

    @OneToMany(mappedBy = "documentType", orphanRemoval = true)
    private List<WorkflowEntity> workflow = new ArrayList<>();

    public List<WorkflowEntity> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<WorkflowEntity> workflow) {
        this.workflow = workflow;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
