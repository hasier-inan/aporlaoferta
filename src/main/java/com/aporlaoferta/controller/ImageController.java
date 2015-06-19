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

import java.io.IOException;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class ImageController {

    private final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageUploadManager imageUploadManager;

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse uploadImage(@RequestParam("file") MultipartFile file) {
        TheResponse result = new TheResponse();
        try {
            String finalUrl = this.imageUploadManager.copyUploadedFileIntoServer(file);
            result.setCode(ResultCode.ALL_OK.getCode());
            result.setResponseResult(ResponseResult.OK);
            result.setDescription(finalUrl);
        } catch (InvalidUploadFolderException | IOException e) {
            LOG.error(e.getMessage());
            result.setCode(ResultCode.IMAGE_UPLOAD_ERROR.getCode());
            result.setResponseResult(ResponseResult.INVALID_DATA_PROVIDED);
            result.setDescription(e.getMessage());
        }
        return result;
    }


}
