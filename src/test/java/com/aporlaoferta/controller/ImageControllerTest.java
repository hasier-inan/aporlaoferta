package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.service.ImageUploadManager;
import com.aporlaoferta.service.InvalidUploadFolderException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.doThrow;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    @InjectMocks
    ImageController imageController;
    @Mock
    ImageUploadManager imageUploadManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvalidFolderDefinedReturnsExpectedResultCode() throws IOException {
        doThrow(new InvalidUploadFolderException("error")).when(this.imageUploadManager)
                .copyUploadedFileIntoServer((MultipartFile) anyObject());
        TheResponse theResponse = imageController.uploadImage(createDummyMultipart());
        assertEquals("Expected Invalid_Upload_Error code", ResultCode.IMAGE_UPLOAD_ERROR.getCode(), theResponse.getCode());
    }

    @Test
    public void testIOExceptionCodeIsReturnedInTheResultCode() throws IOException {
        doThrow(new IOException("error")).when(this.imageUploadManager)
                .copyUploadedFileIntoServer((MultipartFile) anyObject());
        TheResponse theResponse = imageController.uploadImage(createDummyMultipart());
        assertEquals("Expected Invalid_Upload_Error code", ResultCode.IMAGE_UPLOAD_ERROR.getCode(), theResponse.getCode());
    }

    @Test
    public void testFinalUrlIsIncludedInResponse() throws IOException {
        String fileFinalPath = "the.path";
        Mockito.when(this.imageUploadManager.copyUploadedFileIntoServer(any(MultipartFile.class))).thenReturn(fileFinalPath);
        TheResponse theResponse = imageController.uploadImage(createDummyMultipart());
        assertEquals("Expected same path in response description", fileFinalPath, theResponse.getDescription());
    }

    private MultipartFile createDummyMultipart() {
        String imtheData = "oh yez";
        return new MockMultipartFile("fileData", "fileName", "text/plain", imtheData.getBytes());
    }
}
