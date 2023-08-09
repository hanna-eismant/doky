package org.hkurh.doky.documents;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    DocumentEntity create(@NonNull String name, String description);

    Optional<DocumentEntity> find(@NonNull String id);

    @NonNull
    List<DocumentEntity> find();

    void save(@NonNull DocumentEntity document);
}
