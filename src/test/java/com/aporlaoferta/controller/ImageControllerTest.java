package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.service.ImageUploadManager;
import com.aporlaoferta.service.InvalidUploadFolderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    @InjectMocks
    ImageController imageController;
    @Mock
    ImageUploadManager imageUploadManager;
    @Mock
    MultipartFile tooLargeImage;
    @Mock
    File regularFile;

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

    @Ignore
    @Test
    public void testFinalUrlIsIncludedInResponse() throws IOException {
        String fileFinalPath = "the.path";
        when(this.regularFile.getPath()).thenReturn(fileFinalPath);
        when(this.imageUploadManager.copyUploadedFileIntoServer(any(MultipartFile.class))).thenReturn(this.regularFile);
        TheResponse theResponse = imageController.uploadImage(createDummyMultipart());
        assertEquals("Expected same path in response description", fileFinalPath, theResponse.getDescription());
    }

    @Test
    public void testTooLargeImageIsNotSaved() throws IOException {
        when(this.tooLargeImage.getSize()).thenReturn(2000001L);
        TheResponse theResponse = imageController.uploadImage(tooLargeImage);
        verify(this.imageUploadManager, never()).copyUploadedFileIntoServer(any(MultipartFile.class));
        Assert.assertThat("Expected the response to contain image too large code",
                theResponse.getCode(), is(ResultCode.IMAGE_TOO_HEAVY_ERROR.getCode()));
    }

    private MultipartFile createDummyMultipart() {
        String imtheData = "oh yez";
        return new MockMultipartFile("fileData", "fileName", "image/png", imtheData.getBytes());
    }
}
