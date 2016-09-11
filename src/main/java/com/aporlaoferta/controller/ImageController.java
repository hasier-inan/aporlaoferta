package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.service.ImageUploadManager;
import com.aporlaoferta.service.InvalidUploadFolderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class ImageController {

    private static final long MAXIMUM_FILE_SIZE = 4500000L;
    private final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageUploadManager imageUploadManager;

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


    private TheResponse performImageUploadProcess(MultipartFile file) {
        try {
            return updateResultWithSuccessUploadResult(file);
        } catch (InvalidUploadFolderException | IOException | IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return updateResultWithInvalidImageCode(ResultCode.IMAGE_UPLOAD_ERROR, e.getMessage());
        }
    }

    private TheResponse updateResultWithSuccessUploadResult(MultipartFile file) throws IOException {
        File finalFile = null;
        TheResponse result = new TheResponse();
        try {
            finalFile = this.imageUploadManager.copyUploadedFileIntoServer(file);
            String uploadPath = this.imageUploadManager.transformAndUploadFile(finalFile);
            updateResultWithUploadPath(result, uploadPath);
        } catch (InvalidUploadFolderException | IOException e) {
            return ResponseResultHelper
                    .responseResultWithResultCodeError(ResultCode.IMAGE_UPLOAD_ERROR, result);
        } finally {
            if (finalFile != null && finalFile.exists()) {
                finalFile.delete();
            }
        }
        return result;
    }

    private void updateResultWithUploadPath(TheResponse result, String uploadPath) {
        result.setCode(ResultCode.ALL_OK.getCode());
        result.setResponseResult(ResponseResult.OK);
        result.setDescription(uploadPath);
    }


    private TheResponse updateResultWithInvalidImageCode(ResultCode resultCode, String description) {
        TheResponse result = new TheResponse();
        result.setCode(resultCode.getCode());
        result.setResponseResult(resultCode.getResponseResult());
        result.setDescription(description);
        return result;
    }
}
