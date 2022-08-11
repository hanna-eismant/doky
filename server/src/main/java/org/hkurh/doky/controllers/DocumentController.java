package org.hkurh.doky.controllers;


import org.hkurh.doky.facades.DocumentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private DocumentFacade documentFacade;

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @PathVariable String id) {
        System.out.println(file.getOriginalFilename());
        System.out.println(id);

        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DocumentRequest document) {
        var createdDocument = getDocumentFacade().createDocument(document.getName(), document.getDescription());
        var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(createdDocument.getId());

        return ResponseEntity.created(resourceLocation).build();
    }

    public DocumentFacade getDocumentFacade() {
        return documentFacade;
    }

    @Autowired
    public void setDocumentFacade(DocumentFacade documentFacade) {
        this.documentFacade = documentFacade;
    }
}
