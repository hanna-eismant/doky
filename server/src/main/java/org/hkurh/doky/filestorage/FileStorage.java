package org.hkurh.doky.filestorage;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorage {

    void saveFile(@NonNull MultipartFile file, @NonNull String filePath, @NonNull String fileName) throws IOException;

    void saveFile(@NonNull MultipartFile file, @NonNull String filePathWithName) throws IOException;

    Path getFile(@NonNull String filePath) throws IOException;

    /**
     * Check if file with specified path exists in storage.
     *
     * @return <code>true</code> - if file exists,
     * <code>false</code> if file does not exist or {@param filePath} is null or empty
     */
    boolean checkExistence(@Nullable String filePath);
}
