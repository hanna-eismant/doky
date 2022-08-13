package org.hkurh.doky.facades;

import java.net.MalformedURLException;
import java.util.List;

import org.hkurh.doky.dto.DocumentDto;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentFacade {
    DocumentDto createDocument(@NonNull String name, String description);

    DocumentDto findDocument(@NonNull String id);

    List<DocumentDto> findAllDocuments();

    void saveFile(@NonNull MultipartFile file, @NonNull String id);

    @Nullable
    Resource getFile(@NonNull String id) throws MalformedURLException;
}
