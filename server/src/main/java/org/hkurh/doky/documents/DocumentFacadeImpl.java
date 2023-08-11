package org.hkurh.doky.documents;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.errorhandling.DokyNotFoundException;
import org.hkurh.doky.filestorage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.hkurh.doky.MapperFactory.getModelMapper;

@Component
public class DocumentFacadeImpl implements DocumentFacade {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFacadeImpl.class);

    private final DocumentService documentService;
    private final FileStorageService fileStorageService;

    public DocumentFacadeImpl(DocumentService documentService, FileStorageService fileStorageService) {
        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public DocumentDto createDocument(@NonNull String name, String description) {
        var documentEntity = documentService.create(name, description);
        LOG.debug(format("Created new Document [%s]", documentEntity.getId()));
        var documentDto = getModelMapper()
                .map(documentEntity, DocumentDto.class);
        return documentDto;
    }

    @Override
    public DocumentDto findDocument(@NonNull String id) {
        var documentEntity = documentService.find(id);
        var documentDto = documentEntity
                .map(entity -> getModelMapper().map(entity, DocumentDto.class))
                .orElse(null);
        return documentDto;
    }

    @Override
    public List<DocumentDto> findAllDocuments() {
        var documentEntityList = documentService.find();
        var documentDtoList = documentEntityList.stream()
                .map(entity -> getModelMapper().map(entity, DocumentDto.class))
                .collect(Collectors.toList());
        return documentDtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveFile(@NonNull MultipartFile file, @NonNull String id) {
        var documentOpt = documentService.find(id);
        if (documentOpt.isPresent()) {
            try {
                var document = documentOpt.get();
                var path = fileStorageService.store(file, document.getFilePath());
                document.setFilePath(path);
                documentService.save(document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DokyNotFoundException(format("Document with id [%s] not found", id));
        }
    }

    @Override
    public @Nullable Resource getFile(@NonNull String id) throws IOException {
        var documentOpt = documentService.find(id);
        if (documentOpt.isPresent() && StringUtils.isNotBlank(documentOpt.get().getFilePath())) {
            var filePath = documentOpt.get().getFilePath();
            var file = fileStorageService.getFile(filePath);
            if (file != null) {
                LOG.debug(format("Download file for Document [%s] with URI [%s]", id, file.toUri()));
                return new UrlResource(file.toUri());
            } else {
                LOG.warn(format("File [%s] attached to document [%s] does not exists in storage", filePath, id));
                return null;
            }
        } else {
            LOG.debug(format("No attached file for Document [%s]", id));
            return null;
        }
    }
}
