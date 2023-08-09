package org.hkurh.doky.filestorage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.lang.String.format;

@Service
@ConditionalOnProperty(name = "doky.filestorage.type", havingValue = "local-filesystem", matchIfMissing = true)
public class DokyLocalFilesystemStorage implements FileStorage {

    private static final Log LOG = LogFactory.getLog(DokyLocalFilesystemStorage.class);

    private static void saveFileToFilesystem(MultipartFile file, Path path) throws IOException {
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        LOG.debug(format("Save uploaded file to [%s]", path.toAbsolutePath()));
    }

    @Override
    public void saveFile(@NonNull MultipartFile file, @NonNull String filePathWithName) throws IOException {
        var path = Paths.get(filePathWithName);
        saveFileToFilesystem(file, path);
    }

    @Override
    public void saveFile(@NonNull MultipartFile file, @NonNull String filePath, @NonNull String fileName) throws IOException {
        var folder = Paths.get(filePath);
        Files.createDirectories(folder);
        var path = folder.resolve(fileName);
        saveFileToFilesystem(file, path);
    }

    @Override
    public Path getFile(@NonNull String filePath) {
        var file = Paths.get(filePath);
        if (Files.exists(file)) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public boolean checkExistence(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        var file = Paths.get(filePath);
        return Files.exists(file);
    }
}
