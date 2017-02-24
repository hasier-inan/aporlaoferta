package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class ImageController extends ImageHelper {

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
}
