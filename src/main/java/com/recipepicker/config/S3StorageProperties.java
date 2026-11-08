package com.recipepicker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.s3")
public record S3StorageProperties(String bucket, String region, String keyPrefix) {
}
