package org.hkurh.doky.services.impl;

import org.hkurh.doky.entities.DocumentEntity;
import org.hkurh.doky.repositories.DocumentEntityRepository;
import org.hkurh.doky.services.DocumentService;
import org.hkurh.doky.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private DocumentEntityRepository documentEntityRepository;
    private UserService userService;

    @Override
    public DocumentEntity create(@NonNull String name, String description) {
        var document = new DocumentEntity();
        document.setName(name);
        document.setDescription(description);
        var currentUser = getUserService().getCurrentUser();
        document.setCreator(currentUser);
        LOG.debug(format("Created new Document [%s] by User [%s]", document.getId(), currentUser.getId()));
        return getDocumentEntityRepository().save(document);
    }

    @Override
    public Optional<DocumentEntity> find(@NonNull String id) {
        var documentId = Long.parseLong(id);
        var currentUser = userService.getCurrentUser();
        return documentEntityRepository.findByIdAndCreatorId(documentId, currentUser.getId());
    }

    @Override
    public @NonNull List<DocumentEntity> find() {
        var currentUser = userService.getCurrentUser();
        var documentList = new ArrayList<>(documentEntityRepository.findByCreatorId(currentUser.getId()));

        return documentList;
    }

    @Override
    public void save(@NonNull DocumentEntity document) {
        getDocumentEntityRepository().save(document);
    }

    private DocumentEntityRepository getDocumentEntityRepository() {
        return documentEntityRepository;
    }

    @Autowired
    public void setDocumentEntityRepository(DocumentEntityRepository documentEntityRepository) {
        this.documentEntityRepository = documentEntityRepository;
    }

    private UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
