package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.service.ImageUploadManager;
import com.aporlaoferta.service.InvalidUploadFolderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class ImageController {

    private static final long MAXIMUM_FILE_SIZE = 2000000L;
    private final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse uploadImage(@RequestParam("file") MultipartFile file) {
        Long size = file.getSize();
        if (size > MAXIMUM_FILE_SIZE) {
            return updateResultWithInvalidImageCode(
                    ResultCode.IMAGE_TOO_HEAVY_ERROR,
                    ResultCode.IMAGE_TOO_HEAVY_ERROR.getResultDescriptionEsp());
        }
        return performImageUploadProcess(file);
    }

    @Autowired
    private ImageUploadManager imageUploadManager;

    private TheResponse performImageUploadProcess(MultipartFile file) {
        try {
            return updateResultWithSuccessUploadResult(file);
        } catch (InvalidUploadFolderException | IOException e) {
            LOG.error(e.getMessage());
            return updateResultWithInvalidImageCode(ResultCode.IMAGE_UPLOAD_ERROR, e.getMessage());
        }
    }

    private TheResponse updateResultWithSuccessUploadResult(MultipartFile file) throws IOException {
        TheResponse result = new TheResponse();
        File finalFile = this.imageUploadManager.copyUploadedFileIntoServer(file);
        if (invalidMimeType(finalFile)) {
            removeFile(finalFile);
            throw new IOException("Invalid file type");
        }
        if (transformAndUpdateImage(result, finalFile)) {
            return result;
        }
        throw new IOException("Could not parse image");
    }

    private boolean transformAndUpdateImage(TheResponse result, File finalFile) throws IOException {
        String theFinalPath = finalFile.getAbsolutePath() + "_.jpg";
        if (this.imageUploadManager.transformImage(finalFile, theFinalPath)) {
            File alteredImage = new File(theFinalPath);
            Files.copy(alteredImage.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            alteredImage.delete();
            result.setCode(ResultCode.ALL_OK.getCode());
            result.setResponseResult(ResponseResult.OK);
            result.setDescription(this.imageUploadManager.obtainServerPathForImage(finalFile));
            return true;
        }
        return false;
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

    private TheResponse updateResultWithInvalidImageCode(ResultCode resultCode, String description) {
        TheResponse result = new TheResponse();
        result.setCode(resultCode.getCode());
        result.setResponseResult(resultCode.getResponseResult());
        result.setDescription(description);
        return result;
    }


}
