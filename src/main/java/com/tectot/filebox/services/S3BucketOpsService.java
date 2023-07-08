package com.tectot.filebox.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

@Service
public class S3BucketOpsService {

    public void uploadFile(S3Client s3Client, String bucketName, String key) throws IOException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        String fileContent = "Sample text for demo purpose. ";
        File localFile = createLocalTextFile(fileContent);
        s3Client.putObject(objectRequest, RequestBody.fromFile(localFile));
    }

    public void downloadFile(S3Client s3Client, String bucketName, String key){
        String localFilePath = "/Users/gkumar591/Downloads/files/file.txt";

        try {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            saveToFile(objectBytes, localFilePath);

            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }

    private static File createLocalTextFile(String content) {
        try {
            File file = File.createTempFile("file", ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void saveToFile(ResponseBytes<GetObjectResponse> objectBytes, String filePath) throws IOException {
        byte[] data = objectBytes.asByteArray();

        // Write the data to a local file.
        File myFile = new File(filePath);
        OutputStream os = new FileOutputStream(myFile);
        os.write(data);

        os.close();
    }
}
