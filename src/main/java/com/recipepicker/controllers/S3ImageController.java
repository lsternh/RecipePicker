package com.recipepicker.controllers;

import com.recipepicker.services.S3ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class S3ImageController {
    private final S3ImageStorageService storageService;

    public S3ImageController(S3ImageStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image to S3")
    public ResponseEntity<S3ImageStorageService.StoredImage> upload(@RequestParam("file") MultipartFile file)
            throws IOException {
        return ResponseEntity.status(201).body(storageService.store(file));
    }

    @GetMapping("/{*key}")
    @Operation(summary = "Stream an image from S3")
    public ResponseEntity<InputStreamResource> get(@PathVariable String key) {
        S3ImageStorageService.ImageObject image = storageService.load(key);
        MediaType contentType = image.contentType() == null
                ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(image.contentType());
        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(image.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().build().toString())
                .body(new InputStreamResource(image.content()));
    }
}
