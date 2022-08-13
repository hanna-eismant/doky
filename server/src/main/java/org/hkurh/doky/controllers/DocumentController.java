package org.hkurh.doky.controllers;


import java.net.MalformedURLException;

import org.hkurh.doky.facades.DocumentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        getDocumentFacade().saveFile(file, id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable String id) {
        try {
            var file = getDocumentFacade().getFile(id);
            if (file == null) {
                return ResponseEntity.noContent().build();
            }
            var header = new StringBuilder().append("attachment; filename=\"")
                    .append(file.getFilename())
                    .append("\"").toString();
            var response = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, header)
                    .body(file);
            return response;
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DocumentRequest document) {
        var createdDocument = getDocumentFacade().createDocument(document.getName(), document.getDescription());
        var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(createdDocument.getId());

        return ResponseEntity.created(resourceLocation).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        var documents = getDocumentFacade().findAllDocuments();

        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(documents);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        var document = getDocumentFacade().findDocument(id);

        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    public DocumentFacade getDocumentFacade() {
        return documentFacade;
    }

    @Autowired
    public void setDocumentFacade(DocumentFacade documentFacade) {
        this.documentFacade = documentFacade;
    }
}