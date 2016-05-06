package com.aporlaoferta.service;

import com.aporlaoferta.controller.ResponseResult;
import com.aporlaoferta.controller.ResultCode;
import com.aporlaoferta.model.TheResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
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

    public File copyUploadedFileIntoServer(MultipartFile file, String filePath) throws IOException {
        File finalFile = copyFileToServerSide(file, filePath);
        if (invalidMimeType(finalFile)) {
            removeFile(finalFile);
            throw new IOException("Invalid file type");
        }
        return finalFile;

    }

    public TheResponse transformAndUploadFile(File finalFile, String filePath) throws IOException{
        TheResponse result = new TheResponse();
        String serverPath = this.uploadFolder + "/" + filePath;
        if (transformAndUpload(result, filePath, finalFile, serverPath)) {
            return result;
        }
        throw new InvalidUploadFolderException("Could not upload file:");
    }

    private boolean transformAndUpload(TheResponse result, String filePath, File finalFile, String serverPath) throws IOException {
        if (transformAndUploadImage(finalFile) && this.awss3Service.uploadFile(finalFile, serverPath)) {
            result.setCode(ResultCode.ALL_OK.getCode());
            result.setResponseResult(ResponseResult.OK);
            result.setDescription(this.uploadFolderMap + "/" + filePath);
            return true;
        }
        return false;
    }

    private boolean transformAndUploadImage(File finalFile) throws IOException {
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

    public String createCustomFilePath(String fileName) {
        String subFolder = String.valueOf((int) (Math.random() * this.folderDepth));
        return subFolder +
                UUID.randomUUID().toString() +
                fileExtension(fileName);
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
