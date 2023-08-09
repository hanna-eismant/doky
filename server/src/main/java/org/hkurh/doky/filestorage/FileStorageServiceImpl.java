package org.hkurh.doky.filestorage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Log LOG = LogFactory.getLog(FileStorageServiceImpl.class);
    public static final String STORAGE_PATH_PROPERTY = "doky.filestorage.path";
    public static final String DEFAULT_STORAGE_PATH = "./mediadata";

    private Environment environment;
    private FileStorage fileStorage;

    @Override
    public String store(@NonNull MultipartFile file, @NonNull String filePath) throws IOException {
        var isFileExists = getFileStorageStrategy().checkExistence(filePath);
        if (isFileExists) {
            getFileStorageStrategy().saveFile(file, filePath);
            return filePath;
        } else {
            var extension = FilenameUtils.getExtension(file.getOriginalFilename());
            var storagePath = getStoragePath();
            var fileName = generateFileName(extension);
            var generatedFilePath = storagePath + fileName;
            getFileStorageStrategy().saveFile(file, storagePath, fileName);
            return generatedFilePath;
        }
    }

    @Override
    public Path getFile(@NonNull String filePath) throws IOException {
        return getFileStorageStrategy().getFile(filePath);
    }

    private String getStoragePath() {
        var basePath = getEnvironment().getProperty(STORAGE_PATH_PROPERTY, DEFAULT_STORAGE_PATH);
        var today = DateTime.now(DateTimeZone.getDefault());
        var fullPath = basePath + File.separator
                + today.getYear() + File.separator
                + today.getMonthOfYear() + File.separator;

        return fullPath;
    }

    private String generateFileName(String extension) {
        var randomName = RandomStringUtils.random(10, true, true);
        return randomName + "." + extension;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public FileStorage getFileStorageStrategy() {
        return fileStorage;
    }

    @Autowired
    public void setFileStorageStrategy(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }
}
