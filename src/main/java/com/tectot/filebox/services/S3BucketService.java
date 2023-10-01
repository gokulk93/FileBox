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

    private static final Logger logger =  LoggerFactory.getLogger(S3BucketService.class);
    private final S3Client s3Client;

    public S3BucketService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // Create a bucket by using a S3Waiter object
    public boolean createBucket(String bucketName) {
        try {
            S3Waiter s3Waiter = s3Client.waiter();

            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);

            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);

            return waiterResponse.matched().response().isPresent();

        } catch (S3Exception e) {
            logger.error("Error creating bucket '{}': {}", bucketName, e.awsErrorDetails().errorMessage());
            throw e;
        }
    }


    public List<String> getBucketList() {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);

        return listBucketsResponse.buckets().stream()
                .map(Bucket::name)
                .collect(Collectors.toList());
    }


    public void deleteAllObjectsByBucket(String bucketName) {
        try {
            // To delete a bucket, all the objects in the bucket must be deleted first.
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

                List<ObjectIdentifier> objectsToDelete = listObjectsV2Response.contents().stream()
                        .map(object -> ObjectIdentifier.builder()
                                .key(object.key())
                                .build())
                        .collect(Collectors.toList());

                DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(Delete.builder().objects(objectsToDelete).build())
                        .build();

                s3Client.deleteObjects(deleteObjectsRequest);

            } while (listObjectsV2Response.isTruncated());
        }catch (S3Exception e){
            logger.error("Exception while deleting bucket objects: ", e.awsErrorDetails().errorMessage());
        }
    }

    public void deleteBucketByName(String bucketName){
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucketName)
                .build();
        s3Client.deleteBucket(deleteBucketRequest);
    }
}
