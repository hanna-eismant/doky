package org.hkurh.doky.facades;

import org.hkurh.doky.dto.DocumentDto;
import org.springframework.lang.NonNull;

public interface DocumentFacade {
    DocumentDto createDocument(@NonNull String name, String description);
}
