package com.pitthungama.pitthungama_rest.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3BucketStorageService {

    private Logger logger = LoggerFactory.getLogger(S3BucketStorageService.class);

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${application.amazons3bucket.name}")
    private String bucketName;

    @SneakyThrows
    public void uploadFile(MultipartFile file) {
        try {
            String filename = "temp-uploads/" + new RandomString(10).nextString() + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucketName, filename, file.getInputStream(), metadata);
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

    public S3Object getFile(String filename) {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucketName, filename);
            return s3Object;
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

    public S3ObjectInputStream getFileStream(String filename) {
        S3ObjectInputStream inputStream = this.getFile(filename).getObjectContent();
        return inputStream;
    }

    public void removeFile(String filename) {
        try {
            amazonS3Client.deleteObject(bucketName, "temp-uploads/" + filename);
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

    public void moveFile(String currentFilename, String desalinatedFilename) {
        try {
            amazonS3Client.copyObject(bucketName, currentFilename, bucketName, desalinatedFilename);
            amazonS3Client.deleteObject(bucketName, currentFilename);
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

    public void moveFileAsPublicRead(String currentFilename, String desalinatedFilename) {
        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, currentFilename, bucketName, desalinatedFilename);
            copyObjectRequest.setCannedAccessControlList(CannedAccessControlList.PublicRead);
            amazonS3Client.copyObject(copyObjectRequest);
            amazonS3Client.deleteObject(bucketName, currentFilename);
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

    public void copyFile(String currentFilename, String desalinatedFilename) {
        try {
            amazonS3Client.copyObject(bucketName, currentFilename, bucketName, desalinatedFilename);
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }

//    We can use generatePresignedUrl method on our AmazonS3 client bean to generate PresignedUrl which will be valid till provided time.
//    public String presignedUrl(String fileName) throws IOException {
//        return amazonS3Client
//                .generatePresignedUrl(bucketName, fileName, convertToDateViaInstant(LocalDate.now().plusDays(1)))
//                .toString();// URL will be valid for 24hrs
//    }
}
