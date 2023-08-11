package org.hkurh.doky.filestorage;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static java.lang.String.format;

@Service
@ConditionalOnProperty(name = "doky.filestorage.type", havingValue = "azure-blob", matchIfMissing = false)
public class DokyAzureBlobStorage implements FileStorage {

    private static final Log LOG = LogFactory.getLog(DokyAzureBlobStorage.class);

    @Value("${doky.filestorage.azure.connection}")
    private String connectionString;
    @Value("${doky.filestorage.azure.container}")
    private String containerName;
    private BlobContainerClient blobContainerClient;

    @PostConstruct
    public void init() {
        LOG.debug(format("Azure Blob container name [%s] initialized", containerName));
        blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    public void saveFile(@NonNull MultipartFile file, @NonNull String filePathWithName) throws IOException {
        var blobClient = blobContainerClient.getBlobClient(filePathWithName);
        blobClient.upload(file.getInputStream());
    }

    @Override
    public void saveFile(@NonNull MultipartFile file, @NonNull String filePath, @NonNull String fileName) throws IOException {
        saveFile(file, filePath + fileName);
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

    @Override
    public boolean checkExistence(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        var blobClient = blobContainerClient.getBlobClient(filePath);
        return blobClient.exists();
    }
}
