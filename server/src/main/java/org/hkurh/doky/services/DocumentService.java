package org.hkurh.doky.services;

import java.util.List;
import java.util.Optional;

import org.hkurh.doky.entities.DocumentEntity;
import org.springframework.lang.NonNull;

public interface DocumentService {
    DocumentEntity create(@NonNull String name, String description);

    Optional<DocumentEntity> find(@NonNull String id);

    @NonNull
    List<DocumentEntity> find();

    void save(@NonNull DocumentEntity document);
}
