package com.aporlaoferta.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by hasiermetal on 05/05/16.
 */
public class AWSS3Service {
    private final Logger LOG = LoggerFactory.getLogger(AWSS3Service.class);

    private final String bucket;
    private final String bucketFolder;

    public AWSS3Service(String bucket, String bucketFolder) {
        this.bucket = bucket;
        this.bucketFolder = bucketFolder;
    }

    public boolean uploadFile(File fileToBeUploaded, String path) {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            s3client.putObject(createObjectPutRequest(fileToBeUploaded, path));
            return true;
        } catch (AmazonClientException ase) {
            LOG.error("Could not upload file to s3:", ase);
        }
        return false;
    }

    private PutObjectRequest createObjectPutRequest(File fileToBeUploaded, String path) {
        return new PutObjectRequest(
                this.bucket, String.format("%s/%s", this.bucketFolder, path), fileToBeUploaded)
                .withCannedAcl(CannedAccessControlList.PublicRead);
    }
}
