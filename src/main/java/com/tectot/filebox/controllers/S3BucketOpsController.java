package com.tectot.filebox.controllers;

import com.tectot.filebox.services.S3BucketOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@RestController
@RequestMapping(value = "bucketOps")
public class S3BucketOpsController {

    private final S3BucketOpsService s3BucketOpsService;

    private final S3Client s3Client;

    @Autowired
    public S3BucketOpsController(S3BucketOpsService s3BucketOpsService, S3Client s3Client) {
        this.s3BucketOpsService = s3BucketOpsService;
        this.s3Client = s3Client;
    }


    // Upload a file to S3
    @PostMapping(value = "upload", consumes = "application/json")
    public String uploadFile(@RequestParam(name = "bucketName") String bucketName){
        try {
            s3BucketOpsService.uploadFile(bucketName, "file.txt");
        }catch (IOException e){
            return "some exception occurred";
        }
        return "File uploaded successfully";
    }

}
