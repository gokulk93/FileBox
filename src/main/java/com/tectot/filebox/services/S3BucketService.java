package com.tectot.filebox.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3BucketService {

    private static Logger logger =  LoggerFactory.getLogger(S3BucketService.class);


    // Create a bucket by using a S3Waiter object
    public  void createBucket(S3Client s3Client, String bucketName) {

        try {
            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Wait until the bucket is created and print out the response.
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName +" is ready");

        } catch (S3Exception e) {
            logger.error(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public List<String> getBucketList(S3Client s3Client){
        // List buckets
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
        return listBucketsResponse.buckets().stream().map(x -> x.name()).collect(Collectors.toList());

    }

    public void deleteAllObjectsByBucket(S3Client s3Client, String bucketName) {
        try {
            // To delete a bucket, all the objects in the bucket must be deleted first.
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    DeleteObjectRequest request = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Object.key())
                            .build();
                    s3Client.deleteObject(request);
                }
            } while (listObjectsV2Response.isTruncated());
        }catch (S3Exception e){
            logger.error("Exception while deleting bucket objects: ", e.awsErrorDetails().errorMessage());
        }
    }

    public void deleteEmptyBucket(S3Client s3Client, String bucketName){
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucketName)
                .build();
        s3Client.deleteBucket(deleteBucketRequest);
    }
}
