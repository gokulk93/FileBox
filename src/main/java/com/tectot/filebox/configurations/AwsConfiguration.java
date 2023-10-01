package com.tectot.filebox.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.utils.AttributeMap;

import javax.annotation.PostConstruct;
import java.net.URI;

@Configuration
public class AwsConfiguration {
    private FileBoxConfig fileBoxConfig;
    private String AWS_LOCALSTACK_ENDPOINT;
    private String AWS_ACCESS_KEY;
    private String AWS_SECRET;
    private Region AWS_REGION;

    private AwsBasicCredentials AWS_CREDENTIALS;


    @Autowired
    public AwsConfiguration(FileBoxConfig fileBoxConfig) {
        this.fileBoxConfig = fileBoxConfig;
    }

    @PostConstruct
    private void init() {
        AWS_LOCALSTACK_ENDPOINT = fileBoxConfig.getAws().getEndpoint();
        AWS_ACCESS_KEY = fileBoxConfig.getAws().getAccessKey();
        AWS_SECRET = fileBoxConfig.getAws().getSecret();
        AWS_REGION = Region.of(fileBoxConfig.getAws().getRegion());

        AWS_CREDENTIALS = AwsBasicCredentials
                .create(AWS_ACCESS_KEY, AWS_SECRET);
    }

    @Bean
    public S3Client getS3ClientLocal() {
        SdkHttpClient httpClient = UrlConnectionHttpClient.builder()
                .buildWithDefaults(AttributeMap.empty());

        return S3Client.builder()
                .region(AWS_REGION)
                .endpointOverride(URI.create(AWS_LOCALSTACK_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(AWS_CREDENTIALS))
                .httpClient(httpClient)
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }
}
