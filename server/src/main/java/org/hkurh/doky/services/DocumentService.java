package org.hkurh.doky.services;

import org.hkurh.doky.entities.DocumentEntity;
import org.springframework.lang.NonNull;

public interface DocumentService {
    DocumentEntity create(@NonNull String name, String description);
}
