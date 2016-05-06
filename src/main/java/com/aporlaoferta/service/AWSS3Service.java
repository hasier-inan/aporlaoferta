package com.aporlaoferta.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by hasiermetal on 05/05/16.
 */
public class AWSS3Service {
    private final Logger LOG = LoggerFactory.getLogger(AWSS3Service.class);

    private final String bucket;

    public AWSS3Service(String bucket) {
        this.bucket = bucket;
    }

    public boolean uploadFile(File fileToBeUploaded, String path) {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            LOG.info(String.format("FULE WILL BE UPLOADED TO  %s bucket, path is %s, file is at %s ",
                    this.bucket, path, fileToBeUploaded.getAbsolutePath()));
            s3client.putObject(
                    new PutObjectRequest(
                            this.bucket, path, fileToBeUploaded)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
            return true;
        } catch (AmazonClientException ase) {
            LOG.error("Could not upload file to s3:", ase);
        }
        return false;
    }
}
