package org.hkurh.doky.facades.impl;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.DocumentDto;
import org.hkurh.doky.exceptions.DokyNotFoundException;
import org.hkurh.doky.facades.DocumentFacade;
import org.hkurh.doky.facades.MapperFactory;
import org.hkurh.doky.services.DocumentService;
import org.hkurh.doky.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class DocumentFacadeImpl implements DocumentFacade {

    private DocumentService documentService;
    private FileStorageService fileStorageService;

    @Override
    public DocumentDto createDocument(@NonNull String name, String description) {
        var documentEntity = getDocumentService().create(name, description);
        return MapperFactory.getModelMapper().map(documentEntity, DocumentDto.class);
    }

    @Override
    public DocumentDto findDocument(@NonNull String id) {
        var documentEntity = getDocumentService().find(id);

        return documentEntity.map(entity -> MapperFactory.getModelMapper().map(entity, DocumentDto.class)).orElse(null);
    }

    @Override
    public List<DocumentDto> findAllDocuments() {
        var documentEntityList = getDocumentService().find();

        var documentDtoList = documentEntityList.stream()
                .map(entity -> MapperFactory.getModelMapper().map(entity, DocumentDto.class))
                .collect(Collectors.toList());

        return documentDtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveFile(@NonNull MultipartFile file, @NonNull String id) {
        var documentOpt = getDocumentService().find(id);
        if (documentOpt.isPresent()) {
            try {
                var path = getFileStorageService().store(file);
                var document = documentOpt.get();
                document.setFilePath(path);
                getDocumentService().save(document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DokyNotFoundException(format("Document with id [%s] not found", id));
        }
    }

    @Override
    public @Nullable Resource getFile(@NonNull String id) throws MalformedURLException {
        var documentOpt = getDocumentService().find(id);

        if (documentOpt.isPresent() && StringUtils.isNotBlank(documentOpt.get().getFilePath())) {
            var file = getFileStorageService().getFile(documentOpt.get().getFilePath());
            return new UrlResource(file.toUri());
        } else {
            return null;
        }
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public FileStorageService getFileStorageService() {
        return fileStorageService;
    }

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
}
