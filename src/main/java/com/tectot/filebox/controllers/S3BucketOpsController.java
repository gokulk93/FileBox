package com.tectot.filebox.controllers;

import com.tectot.filebox.services.S3BucketOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@RestController
@RequestMapping(value = "bucketOps")
public class S3BucketOpsController {

    @Autowired
    private S3BucketOpsService s3BucketOpsService;

    @Autowired
    private S3Client s3Client;


    @PostMapping(value = "upload", consumes = "application/json")
    public String uploadFile(@RequestParam(name = "bucketName") String bucketName){
        try {
            s3BucketOpsService.uploadFile(s3Client, bucketName, "file.txt");
        }catch (IOException e){
            return "some excception occured";
        }
        return "File uploaded successfully";
    }

    @GetMapping(value = "download", consumes = "application/json")
    public String downloadFile(@RequestParam(name = "bucketName") String bucketName){
        s3BucketOpsService.downloadFile(s3Client, bucketName, "file.txt");
        return "File downloaded successfully";
    }

}
