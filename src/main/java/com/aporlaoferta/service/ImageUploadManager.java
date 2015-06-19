package com.aporlaoferta.service;

import com.aporlaoferta.utils.OsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class ImageUploadManager {

    private final Logger LOG = LoggerFactory.getLogger(ImageUploadManager.class);
    private final String uploadFolder;
    private final int folderDepth;

    public ImageUploadManager(String uploadFolder, int folderDepth) {
        this.uploadFolder = uploadFolder;
        this.folderDepth = folderDepth;
        File uploadDir = new File(this.uploadFolder);
        if (!uploadDir.exists()) {
            LOG.warn("Upload folder not found on server side: {}", uploadDir.getAbsolutePath());
            if (!uploadDir.mkdir()) {
                String message = String.format("Could not create upload folder in the following path: %s", uploadDir.getAbsolutePath());
                throw new InvalidUploadFolderException(message);
            }
        }
    }

    public String copyUploadedFileIntoServer(MultipartFile file) throws IOException {
        String filePath = creteCustomFilePath(file.getOriginalFilename());
        file.transferTo(new File(filePath));
        return filePath;
    }

    private String creteCustomFilePath(String fileName) {
        String imageSubfolder = this.uploadFolder +
                OsHelper.osSeparator() + String.valueOf((int) (Math.random() * this.folderDepth));
        File subFolder = new File(imageSubfolder);
        if (!subFolder.exists() && !subFolder.mkdir()) {
            LOG.error("Could not create upload subfolder in the following path: {}", subFolder.getAbsolutePath());
        }
        return
                imageSubfolder +
                        OsHelper.osSeparator() +
                        UUID.randomUUID().toString() +
                        fileExtension(fileName);
    }

    private String fileExtension(String fullPath) {
        return fullPath.substring(fullPath.length() - 4, fullPath.length());
    }
}
