package com.ums.config.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.AwsS3EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {
    private final ConfigServerProperties serverProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 원하는 리전으로 변경
                .credentialsProvider(ProfileCredentialsProvider.create()) // 기본 AWS 인증 정보 사용
                .build();
    }

    @Bean
    public AwsS3EnvironmentRepository awsS3EnvironmentRepository() {
        return new AwsS3EnvironmentRepository(s3Client(), "ums-proj-bucket-01",serverProperties);
    }
}
