package com.recipepicker.services;

import com.recipepicker.config.S3StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class S3ImageStorageService {
    private static final Logger log = LoggerFactory.getLogger(S3ImageStorageService.class);
    private final S3Client s3Client;
    private final S3StorageProperties properties;

    public S3ImageStorageService(S3Client s3Client, S3StorageProperties properties) {
        this.s3Client = s3Client;
        this.properties = properties;
    }

    public StoredImage store(MultipartFile file) throws IOException {
        requireConfiguredBucket();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image file must not be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are accepted");
        }

        String key = normalizedPrefix() + UUID.randomUUID() + extension(file.getOriginalFilename());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.bucket())
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();
        try (InputStream input = file.getInputStream()) {
            s3Client.putObject(request, RequestBody.fromInputStream(input, file.getSize()));
        } catch (Exception e) {
            log.error("Failed to store image in S3 {} ",key, e);
            throw new IllegalArgumentException("Failed to store image in S3: " + key, e);
        }
        return new StoredImage(key, contentType, file.getSize());
    }

    public ImageObject load(String key) {
        requireConfiguredBucket();
        String validatedKey = validateKey(key);
        try {
            ResponseInputStream<GetObjectResponse> content = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(properties.bucket()).key(properties.keyPrefix()+""+validatedKey).build());
            return new ImageObject(content, content.response().contentType(), content.response().contentLength());
        } catch (Exception e) {
            log.error("Failed to retrieve image from S3 {} ",validatedKey, e);
            throw new IllegalArgumentException("Failed to retrieve image from S3: " + validatedKey, e);
        }
    }

    private void requireConfiguredBucket() {
        if (!StringUtils.hasText(properties.bucket())) {
            throw new IllegalStateException("S3 bucket is not configured; set S3_BUCKET or app.s3.bucket");
        }
    }

    private String validateKey(String key) {
        if (key != null && key.startsWith("/")) {
            key = key.substring(1);
        }
        //if (!StringUtils.hasText(key) || key.contains("..") || !key.startsWith(normalizedPrefix())) {
        //    throw new IllegalArgumentException("Invalid image key");
        //}
        return key;
    }

    private String normalizedPrefix() {
        String prefix = properties.keyPrefix() == null ? "" : properties.keyPrefix().trim();
        return prefix.isEmpty() || prefix.endsWith("/") ? prefix : prefix + "/";
    }

    private String extension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        String cleaned = StringUtils.cleanPath(filename);
        int lastDot = cleaned.lastIndexOf('.');
        return lastDot >= 0 ? cleaned.substring(lastDot).replaceAll("[^A-Za-z0-9.]", "") : "";
    }

    public record StoredImage(String key, String contentType, long size) {
    }

    public record ImageObject(InputStream content, String contentType, long contentLength) {
    }
}
