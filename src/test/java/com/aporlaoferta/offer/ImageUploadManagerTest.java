package com.aporlaoferta.offer;

import com.aporlaoferta.service.ImageUploadManager;
import com.aporlaoferta.service.InvalidUploadFolderException;
import com.aporlaoferta.utils.OsHelper;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 19/06/15
 * Time: 11:26
 */
public class ImageUploadManagerTest {

    @Test
    public void testUploadDirectoryIsCreatedIfDoesNotExist() throws Exception {
        new ImageUploadManager("validFolder", 1);
        File validFolder = new File("validFolder");
        if (!validFolder.exists()) {
            fail("Expected folder to exist");
        }
        validFolder.delete();
    }

    @Test(expected = InvalidUploadFolderException.class)
    public void testUploadDirectoryIsNotCreatedIfItIsInvalid() throws Exception {
        new ImageUploadManager("invalid\\/folder", 1);
    }

    @Test
    public void testMultipartFileIsCopiedToUploadFolderAndSubfolder() throws Exception {
        ImageUploadManager imageUploadManager = new ImageUploadManager("validFolder", 0);
        File file = new File(imageUploadManager.copyUploadedFileIntoServer(createDummyMultipart()));
        if (!file.exists()) {
            fail("Expected file to be copied to target folder/subfolder");
        }
        assertTrue("Expected file to exist so that it can be deleted", file.delete());
        deleteTemporaryFolders();

    }

    private MultipartFile createDummyMultipart() {
        String imtheData = "oh yez";
        return new MockMultipartFile("fileData", "fileName", "text/plain", imtheData.getBytes());
    }

    private void deleteTemporaryFolders() {
        assertTrue("Expected subfolder to exist to be deleted", new File("validFolder" + OsHelper.osSeparator() + "0").delete());
        assertTrue("Expected upload folder to exist to be deleted", new File("validFolder").delete());
    }
}
