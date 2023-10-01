package com.tectot.filebox.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private static Logger logger = LoggerFactory.getLogger(FileService.class);
    private final S3Client s3Client;

    @Autowired
    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // Fetch all files from S3 bucket
    public List<S3Object> listFilesFromS3Bucket(String bucketName) {
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> s3ObjectList = s3Client.listObjectsV2(listRequest).contents();
            // You can iterate the s3ObjectList lists to get the key name, size, tag and so on.

            return s3ObjectList;
        } catch (NoSuchBucketException e) {
            // Log the exception or handle it as needed
            logger.error("Error listing files in bucket '{}': {}", bucketName, e.getMessage());
            return new ArrayList<>();
        }
    }


    /*TODO Notes
     * Use ResponseEntity and InputStreamResource: size<100MB - done
     * Use StreamingResponseBody: size >100MB
     * */

    // Download single file from S3 bucket by fileName
    public ResponseBytes<GetObjectResponse> downloadFileBytesFromS3Bucket(String contentName, String bucketName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(contentName)
                .build();
        return s3Client.getObjectAsBytes(getObjectRequest);
    }

    // Upload single file to S3 bucket
    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    .build();

            try (InputStream inputStream = file.getInputStream()) {
                RequestBody requestBody = RequestBody.fromInputStream(inputStream, file.getSize());
                s3Client.putObject(request, requestBody);
            }
            return "File uploaded successfully!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "File upload failed.";
        }

    }

}
