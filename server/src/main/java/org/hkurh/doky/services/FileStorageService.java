package org.hkurh.doky.services;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String store(@NonNull MultipartFile file) throws IOException;

    Path getFile(@NonNull String filePath);
}
