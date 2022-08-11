package org.hkurh.doky.services;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(@NonNull MultipartFile file) throws IOException;
}
