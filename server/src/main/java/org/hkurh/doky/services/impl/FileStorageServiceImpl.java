package org.hkurh.doky.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hkurh.doky.services.FileStorageService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

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

        return filePath.toString();
    }

    private String getStoragePath() {
        var basePath = getEnvironment().getProperty(STORAGE_PATH_PROPERTY, DEFAULT_STORAGE_PATH);
        var today = DateTime.now(DateTimeZone.getDefault());
        var fullPath = new StringBuilder().append(basePath)
                .append(File.separator).append(today.getYear())
                .append(File.separator).append(today.getMonthOfYear())
                .append(File.separator).toString();

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
