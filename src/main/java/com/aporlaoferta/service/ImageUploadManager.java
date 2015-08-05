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
    private final String uploadFolderMap;
    private final int folderDepth;
    private final ImageTransformer imageTransformer;

    public ImageUploadManager(String uploadFolder, String uploadFolderMap, int folderDepth) {
        this.uploadFolder = uploadFolder;
        this.folderDepth = folderDepth;
        this.uploadFolderMap = uploadFolderMap;
        this.imageTransformer = new ImageTransformer();
        File uploadDir = new File(this.uploadFolder);
        if (!uploadDir.exists()) {
            LOG.warn("Upload folder not found on server side: {}", uploadDir.getAbsolutePath());
            if (!uploadDir.mkdir()) {
                String message = String.format("Could not create upload folder in the following path: %s", uploadDir.getAbsolutePath());
                throw new InvalidUploadFolderException(message);
            }
        }
    }

    public String obtainServerPathForImage(File file){
         return this.uploadFolderMap+file.getParentFile().getName()+"/"+file.getName();
    }

    public File copyUploadedFileIntoServer(MultipartFile file) throws IOException {
        String filePath = creteCustomFilePath(file.getOriginalFilename());
        File finalFile = new File(filePath);
        file.transferTo(finalFile);
        //return filePath.replace("\\", "/");
        return finalFile;
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
}
