package org.hkurh.doky.facades.impl;

import org.hkurh.doky.dto.DocumentDto;
import org.hkurh.doky.facades.DocumentFacade;
import org.hkurh.doky.facades.MapperFactory;
import org.hkurh.doky.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DocumentFacadeImpl implements DocumentFacade {

    private DocumentService documentService;

    @Override
    public DocumentDto createDocument(@NonNull String name, String description) {
        var documentEntity = getDocumentService().create(name, description);

        return MapperFactory.getModelMapper().map(documentEntity, DocumentDto.class);
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
}
