package com.tectot.filebox.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;

import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.utils.AttributeMap;

import java.net.URI;
import java.time.Duration;

@Configuration
public class AppConfiguration {

    private final String AWS_LOCALSTACK_ENDPOINT = "http://localhost:4566";

    private ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider
            .create("localstack");

    AwsBasicCredentials credentials = AwsBasicCredentials
            .create("admin", "admin"); // Replace with your LocalStack credentials

    private Region region = Region.US_EAST_1;

    @Bean
    public S3Client getS3ClientLocal(){
        SdkHttpClient httpClient = UrlConnectionHttpClient.builder()
                .buildWithDefaults(AttributeMap.empty());

        return S3Client.builder()
                .region(region)
                .endpointOverride(URI.create(AWS_LOCALSTACK_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .httpClient(httpClient)
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

//    @Bean
//    public S3Client getS3ClientLocal(){
//        return S3Client.builder()
//                .region(region)
//                .endpointOverride(URI.create(AWS_LOCALSTACK_ENDPOINT))
//                .credentialsProvider(credentialsProvider)
//                .build();
//
//    }
}
