package org.hkurh.doky.documents;

import org.hkurh.doky.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentEntityRepository documentEntityRepository;
    private final UserService userService;

    public DocumentServiceImpl(DocumentEntityRepository documentEntityRepository, UserService userService) {
        this.documentEntityRepository = documentEntityRepository;
        this.userService = userService;
    }

    @Override
    public DocumentEntity create(@NonNull String name, String description) {
        var document = new DocumentEntity();
        document.setName(name);
        document.setDescription(description);
        var currentUser = userService.getCurrentUser();
        document.setCreator(currentUser);
        LOG.debug(format("Created new Document [%s] by User [%s]", document.getId(), currentUser.getId()));
        return documentEntityRepository.save(document);
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
        return new ArrayList<>(documentEntityRepository.findByCreatorId(currentUser.getId()));
    }

    @Override
    public void save(@NonNull DocumentEntity document) {
        documentEntityRepository.save(document);
    }
}
