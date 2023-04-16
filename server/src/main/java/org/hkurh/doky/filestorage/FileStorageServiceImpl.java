package org.hkurh.doky.filestorage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.lang.String.format;


@Service
@ConditionalOnProperty(name = "doky.filestorage.type", havingValue = "local-filesystem", matchIfMissing = true)
public class FileStorageServiceImpl implements FileStorageService {

    private static final Log LOG = LogFactory.getLog(FileStorageServiceImpl.class);
    public static final String STORAGE_PATH_PROPERTY = "doky.file.storage.path";
    public static final String DEFAULT_STORAGE_PATH = "./mediadata";

    private Environment environment;

    @Override
    public String store(@NonNull MultipartFile file) throws IOException {
        var randomName = RandomStringUtils.random(10, true, true);
        var extension = FilenameUtils.getExtension(file.getOriginalFilename());
        var folder = Paths.get(getStoragePath());
        Files.createDirectories(folder);
        var filePath = folder.resolve(randomName + "." + extension);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        LOG.debug(format("Save uploaded file to [%s]", filePath.toAbsolutePath()));
        return filePath.toString();
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

    private String getStoragePath() {
        var basePath = getEnvironment().getProperty(STORAGE_PATH_PROPERTY, DEFAULT_STORAGE_PATH);
        var today = DateTime.now(DateTimeZone.getDefault());
        var fullPath = basePath + File.separator
                + today.getYear() + File.separator
                + today.getMonthOfYear() + File.separator;

        return fullPath;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
