package org.hkurh.doky.services.impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hkurh.doky.services.FileStorageService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
@ConditionalOnProperty(name = "doky.filestorage.type", havingValue = "azure-blob", matchIfMissing = false)
public class DokyAzureBlobStorageServiceImpl implements FileStorageService {

    @Value("${doky.filestorage.azure.connection}")
    private String connectionString;
    @Value("${doky.filestorage.azure.container}")
    private String containerName;
    private BlobContainerClient blobContainerClient;

    public DokyAzureBlobStorageServiceImpl() {
        blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    @Override
    public String store(@NonNull MultipartFile file) throws IOException {
        var randomName = RandomStringUtils.random(10, true, true);
        var extension = FilenameUtils.getExtension(file.getOriginalFilename());
        var blobName = getStoragePath() + randomName + "." + extension;
        var blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.upload(file.getInputStream());
        return blobName;
    }

    @Override
    public Path getFile(@NonNull String filePath) throws IOException {
        var blobClient = blobContainerClient.getBlobClient(filePath);
        var now = new DateTime();
        var prefix = now.toString("MM-dd-yyyy") + "-";
        var suffix = "." + FilenameUtils.getExtension(filePath);
        var tempFile = File.createTempFile(prefix, suffix);
        var tempFileOutStream = new FileOutputStream(tempFile);
        blobClient.downloadStream(tempFileOutStream);
        return tempFile.toPath();
    }

    private String getStoragePath() {
        var today = DateTime.now(DateTimeZone.getDefault());
        var fullPath = today.getYear() + File.separator
                + today.getMonthOfYear() + File.separator;
        return fullPath;
    }
}
