package org.hkurh.doky.services.impl;

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
