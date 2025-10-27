package com.ums.config.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.AwsS3EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {
    private final ConfigServerProperties serverProperties;

    @Value("${spring.cloud.config.server.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.config.server.s3.label}")
    private String label;


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 원하는 리전으로 변경
                .credentialsProvider(ProfileCredentialsProvider.create()) // 기본 AWS 인증 정보 사용
                .build();
    }

    @Bean
    public AwsS3EnvironmentRepository awsS3EnvironmentRepository() {
        serverProperties.setDefaultLabel(label);
        return new AwsS3EnvironmentRepository(s3Client(), bucket, true, serverProperties);
    }

}
