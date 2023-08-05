package org.bhavani.constructions.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import static org.bhavani.constructions.constants.Constants.*;
import static org.bhavani.constructions.constants.ErrorConstants.DOC_PARSING_ERROR;

@Slf4j
public class AWSS3Util {

    public static String uploadToAWSS3(InputStream fileContent, String fileName, String filePath){
        AmazonS3 s3Client = getAmazonS3Client();
        String completePath = filePath + generateUUID() + "##" + fileName;

        try {
            PutObjectRequest request = new PutObjectRequest(AWS_BUCKET_NAME, completePath, fileContent, null);
            s3Client.putObject(request);
            log.info("Uploaded Doc: {} to S3", completePath);
        } catch (Exception e) {
            throw new IllegalArgumentException(DOC_PARSING_ERROR);
        } finally {
            try {
                fileContent.close();
            } catch (IOException e) {
                 e.printStackTrace();
            }
        }
        return completePath;
    }

    public static String updateDocInAWS(String existingFIlePath, InputStream fileContent, String fileName, String filePath){
        AmazonS3 s3Client = getAmazonS3Client();
        s3Client.deleteObject(AWS_BUCKET_NAME, existingFIlePath);
        return uploadToAWSS3(fileContent, fileName, filePath);
    }

    public static InputStream getFile(String filePath) {
        if(Objects.isNull(filePath) || filePath.isEmpty())
            return null;
        AmazonS3 s3Client = getAmazonS3Client();
        S3Object s3Object = s3Client.getObject(AWS_BUCKET_NAME, filePath);
        return s3Object.getObjectContent();
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private static AmazonS3 getAmazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }
}
