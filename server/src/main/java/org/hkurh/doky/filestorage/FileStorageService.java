package org.hkurh.doky.filestorage;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {

    /**
     * Save file to storage
     *
     * @return filepath that can be used to download file from storage by {@link #getFile(String)} method
     * @throws IOException
     */
    String store(@NonNull MultipartFile file) throws IOException;

    /**
     * Get file from storage
     *
     * @param filePath filepath to download. It is returned by {@link #store(MultipartFile)} method
     * @return
     */
    Path getFile(@NonNull String filePath) throws IOException;
}
