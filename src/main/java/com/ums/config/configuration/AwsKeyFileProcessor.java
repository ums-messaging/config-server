package com.ums.config.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AwsKeyFileProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try{
            String accessKeyPath = environment.getProperty("AWS_ACCESS_KEY_ID_FILE");
            String secretKeyPath = environment.getProperty("AWS_SECRET_ACCESS_KEY_ID_FILE");
            String region = System.getenv("AWS_REGION");
            String bucket = System.getenv("AWS_BUCKET");


            if(accessKeyPath != null && secretKeyPath != null) {
                String accessKey = Files.readString(Paths.get(accessKeyPath)).trim();
                String secretKey = Files.readString(Paths.get(secretKeyPath)).trim();

                Map<String, Object> map = new HashMap<>();
                map.put("spring.cloud.aws.credentials.access-key", accessKey);
                map.put("spring.cloud.aws.credentials.secret-key", secretKey);
                map.put("spring.cloud.aws.region.static", region);
                map.put("spring.cloud.aws.s3.bucket", bucket);


                environment.getPropertySources().addFirst(new MapPropertySource("aws-keys", map));
            }
        }catch (IOException e) {
            throw new RuntimeException("Failed to read AWS key files", e);

        }
    }
}
