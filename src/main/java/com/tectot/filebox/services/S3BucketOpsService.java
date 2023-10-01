package com.tectot.filebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

@Service
public class S3BucketOpsService {

    private final S3Client s3Client;

    @Autowired
    public S3BucketOpsService(S3Client s3Client) {
        this.s3Client = s3Client;
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

    public void uploadFile(String bucketName, String key) throws IOException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        String fileContent = "Sample text for demo purpose. ";
        File localFile = createLocalTextFile(fileContent);
        s3Client.putObject(objectRequest, RequestBody.fromFile(localFile));
    }

}
