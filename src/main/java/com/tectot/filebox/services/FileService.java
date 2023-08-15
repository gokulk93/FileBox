package com.tectot.filebox.services;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private static Logger logger =  LoggerFactory.getLogger(FileService.class);

    public List<S3Object> listFilesFromS3Bucket(S3Client s3Client, String bucketName){
        try {
            List<String> result = new ArrayList<>();

            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objectSummaries = s3Client.listObjectsV2(listRequest).contents();


            // Process the object summaries
            for (S3Object objectSummary : objectSummaries) {
                String key = objectSummary.key(); // The key represents the object (file) name in the bucket
                long size = objectSummary.size(); // The size of the object in bytes
                String eTag = objectSummary.eTag(); // The ETag (checksum) of the object
                // You can access more properties and metadata of the object summary as needed

                // Perform any processing you need on each object (file) in the bucket
                logger.info(objectSummary.toString());
                result.add(key);
            }
            return objectSummaries;
        }catch (NoSuchBucketException e){
            return new ArrayList<>();
        }
    }


    public boolean downloadFileFromS3Bucket(S3Client s3Client, String key, HttpServletResponse response) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("mytestbucket")
                    .key(key)
                    .build();

            // Download the file using the getObject method on the s3Client.
            ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);

            // Set the appropriate headers for the download.
            response.setContentType(responseInputStream.response().contentType());
            response.setContentLengthLong(responseInputStream.response().contentLength());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + key + "\"");

            // Stream the S3 object's content to the response's output stream.
            IOUtils.copy(responseInputStream, response.getOutputStream());
            response.flushBuffer();
            return true;
        }catch (IOException e){
            logger.error(e.getMessage());
            return false;
        }
    }

    /*TODO
     * The above method for download is not recommended
     * Use ResponseEntity and InputStreamResource: size<100MB
     * Use StreamingResponseBody: size >100MB
     * */

}
