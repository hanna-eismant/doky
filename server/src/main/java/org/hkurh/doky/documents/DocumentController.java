package org.hkurh.doky.documents;


import org.hkurh.doky.security.DokyAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;

import static java.lang.String.format;

@RestController
@RequestMapping("/documents")
@Secured(DokyAuthority.Role.ROLE_USER)
public class DocumentController implements DocumentApi {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

    private DocumentFacade documentFacade;

    @Override
    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @PathVariable String id) {
        getDocumentFacade().saveFile(file, id);
        return ResponseEntity.ok(null);
    }

    @Override
    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable String id) throws IOException {
        try {
            var file = getDocumentFacade().getFile(id);
            if (file == null) {
                LOG.debug(format("No attached file for document [%s]", id));
                return ResponseEntity.noContent().build();
            }
            var header = "attachment; filename=\"" +
                    file.getFilename() +
                    "\"";
            var response = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, header)
                    .body(file);
            return response;
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DocumentRequest document) {
        var createdDocument = getDocumentFacade().createDocument(document.getName(), document.getDescription());
        var resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(createdDocument.getId());

        return ResponseEntity.created(resourceLocation).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getAll() {
        var documents = getDocumentFacade().findAllDocuments();
        if (documents.isEmpty()) {
            LOG.debug("No Documents for current user");
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(documents);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        var document = getDocumentFacade().findDocument(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            LOG.debug(format("No Document with id [%s]", id));
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
