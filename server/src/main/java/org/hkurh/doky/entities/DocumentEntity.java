package org.hkurh.doky.entities;

import javax.persistence.*;

@Entity
@Table(name = "document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentTypeEntity documentType;

    @ManyToOne()
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public DocumentTypeEntity getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeEntity documentType) {
        this.documentType = documentType;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
