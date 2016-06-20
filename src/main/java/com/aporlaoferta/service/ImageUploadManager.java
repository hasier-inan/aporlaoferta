package com.aporlaoferta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class ImageUploadManager {

    private final Logger LOG = LoggerFactory.getLogger(ImageUploadManager.class);

    private final String uploadFolder;
    private final String uploadFolderMap;
    private final int folderDepth;
    private final AWSS3Service awss3Service;
    private final ImageTransformer imageTransformer;

    public ImageUploadManager(String uploadFolder,
                              String uploadFolderMap,
                              int folderDepth,
                              AWSS3Service awss3Service) {

        this.uploadFolder = uploadFolder;
        this.folderDepth = folderDepth;
        this.uploadFolderMap = uploadFolderMap;
        System.setProperty("java.awt.headless", "true");
        this.imageTransformer = new ImageTransformer();
        this.awss3Service = awss3Service;
        createTemporaryUploadFolder();
    }

    private void createTemporaryUploadFolder() {
        File uploadDir = new File(this.uploadFolder);
        if (!uploadDir.exists()) {
            LOG.warn("Upload folder not found on server side: {}", uploadDir.getAbsolutePath());
            if (!uploadDir.mkdir()) {
                String message = String.format("Could not create upload folder in the following path: %s", uploadDir.getAbsolutePath());
                throw new InvalidUploadFolderException(message);
            }
        }
    }

    public File copyUploadedFileIntoServer(MultipartFile file) throws IOException {
        String filePath = createCustomFilePath(file.getOriginalFilename());
        File finalFile = copyFileToServerSide(file, filePath);
        if (invalidMimeType(finalFile)) {
            removeFile(finalFile);
            throw new IOException("Invalid file type");
        }
        return finalFile;

    }

    public String transformAndUploadFile(File finalFile) throws IOException {
        String serverPath = createCustomUploadPath(finalFile.getName());
        if (transformAndUpload(finalFile, serverPath)) {
            return this.uploadFolderMap + "/" + serverPath;
        }
        throw new InvalidUploadFolderException("Could not upload file:");
    }

    private boolean transformAndUpload(File finalFile, String serverPath) throws IOException {
        return transformImage(finalFile)
                && this.awss3Service.uploadFile(finalFile, serverPath);
    }

    private boolean transformImage(File finalFile) throws IOException {
        String theFinalPath = finalFile.getAbsolutePath() + "_.jpg";
        if (transformImage(finalFile, theFinalPath)) {
            File alteredImage = new File(theFinalPath);
            Files.copy(alteredImage.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            alteredImage.delete();
            return true;
        }
        return false;
    }

    private File copyFileToServerSide(MultipartFile file, String filePath) throws IOException {
        File finalFile = new File(filePath);
        file.transferTo(finalFile);
        return finalFile;
    }

    public String createCustomUploadPath(String fileName) {
        String subFolder = String.valueOf((int) (Math.random() * this.folderDepth));
        return String.format("%s/%s%s", subFolder, UUID.randomUUID().toString(), fileExtension(fileName));
    }

    public String createCustomFilePath(String fileName) {
        return this.uploadFolder +
                fileName;
    }


    public boolean transformImage(File imageFile, String fpath) {
        try {
            this.imageTransformer.createSquareImage(imageFile, fpath);
            return true;
        } catch (IOException e) {
            LOG.error("Could not transform image: ", e);
        }
        return false;
    }

    private String fileExtension(String fullPath) {
        return fullPath.substring(fullPath.length() - 4, fullPath.length());
    }

    private boolean invalidMimeType(File f) {
        ConfigurableMimeFileTypeMap configurableMimeFileTypeMap =
                new ConfigurableMimeFileTypeMap();
        String mimeType = configurableMimeFileTypeMap.getContentType(f);
        return !mimeType.substring(0, 5).equalsIgnoreCase("image");
    }

    private void removeFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }


}
