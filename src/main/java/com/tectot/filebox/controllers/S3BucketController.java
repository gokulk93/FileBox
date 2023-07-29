package com.tectot.filebox.controllers;

import com.tectot.filebox.services.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@RestController
@RequestMapping(value = "/buckets")
public class S3BucketController {

    @Autowired
    private S3BucketService s3BucketService;

    @Autowired
    private S3Client s3Client;


    // Creating new bucket
    @PostMapping(value = "/create", consumes = "application/json")
    public String createBucket(@RequestParam(name = "bucketName") String bucketName){
        s3BucketService.createBucket(s3Client, bucketName);
        return "Successfully created s3 bucket";
    }



    // View list of S3 buckets
    @GetMapping(value = "/list", consumes = "application/json")
    public List<String> viewBuckets(){
        return s3BucketService.getBucketList(s3Client);
    }


    // Delete all objects inside  a bucket but not bucket
    @DeleteMapping(value = "objectsByBucket", consumes = "application/json")
    public String deleteObjectsInBucket(@RequestParam(name = "bucketName") String bucketName){
        s3BucketService.deleteAllObjectsByBucket(s3Client,bucketName);
        return "Successfully deleted all objects inside a bucket";
    }



    // Delete whole bucket itself.
    @DeleteMapping(value = "/delete", consumes = "application/json")
    public String deleteBucket(@RequestParam(name = "bucketName") String bucketName){
        s3BucketService.deleteEmptyBucket(s3Client, bucketName);
        return "Successfully deleted s3 bucket";
    }


}
