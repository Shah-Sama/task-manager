package com.example.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SQSConfig {
    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public SqsClient sqsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}