package org.hkurh.doky.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hkurh.doky.entities.DocumentEntity;
import org.hkurh.doky.repositories.DocumentEntityRepository;
import org.hkurh.doky.services.DocumentService;
import org.hkurh.doky.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentEntityRepository documentEntityRepository;
    private UserService userService;

    @Override
    public DocumentEntity create(@NonNull String name, String description) {
        var document = new DocumentEntity();
        document.setName(name);
        document.setDescription(description);

        var currentUser = getUserService().getCurrentUser();
        document.setCreator(currentUser);

        return getDocumentEntityRepository().save(document);
    }

    @Override
    public Optional<DocumentEntity> find(@NonNull String id) {
        var longId = Long.parseLong(id);
        return getDocumentEntityRepository().findById(longId);
    }

    @Override
    public @NonNull List<DocumentEntity> find() {
        var documentList = new ArrayList<DocumentEntity>();
        getDocumentEntityRepository().findAll().forEach(documentList::add);

        return documentList;
    }

    @Override
    public void save(@NonNull DocumentEntity document) {
        getDocumentEntityRepository().save(document);
    }

    public DocumentEntityRepository getDocumentEntityRepository() {
        return documentEntityRepository;
    }

    @Autowired
    public void setDocumentEntityRepository(DocumentEntityRepository documentEntityRepository) {
        this.documentEntityRepository = documentEntityRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
