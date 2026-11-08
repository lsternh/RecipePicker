package com.recipepicker.controllers;

import java.util.UUID;

import com.recipepicker.dto.CreateUploadRequest;
import com.recipepicker.dto.FileUploadResponse;
import com.recipepicker.dto.SplitFileUploadResponse;
import com.recipepicker.entity.FileUploadEntity;
import com.recipepicker.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class FileUploadController {

    private final UploadService uploadService;

	@Autowired
	public FileUploadController(UploadService uploadService) {
        this.uploadService = uploadService;
	}

    @PostMapping("/createUpload")
    public ResponseEntity<FileUploadResponse> createUpload(@RequestBody CreateUploadRequest request) {
        FileUploadResponse response = uploadService.createUpload(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/uploads/split", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SplitFileUploadResponse> splitUpload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("chunkCount") int chunkCount,
            @RequestParam(value = "description", required = false) String description) {
        SplitFileUploadResponse response = uploadService.splitAndSave(file, chunkCount, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/{uploadId}/chunks", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileUploadResponse> uploadChunk(@PathVariable UUID uploadId, @RequestHeader("X-Chunk-Number") int chunkNumber,
            @RequestBody byte[] chunk) {
        FileUploadResponse response = uploadService.appendChunk(uploadId, chunkNumber, chunk);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/uploads/{uploadId}/file")
    public ResponseEntity<byte[]> downloadUpload(@PathVariable UUID uploadId) {
        FileUploadEntity upload = uploadService.getCompletedUpload(uploadId);
        String filename = downloadFilename(upload.getFilename());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, contentDisposition(filename))
                .contentLength(upload.getBody().length)
                .body(upload.getBody());
    }

    private String contentDisposition(String filename) {
        String asciiFallback = filename.replaceAll("[^\\x20-\\x7E]", "_")
                .replace("\\", "_")
                .replace("\"", "_");
        String utf8Filename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        return "attachment; filename=\"" + asciiFallback + "\"; filename*=UTF-8''" + utf8Filename;
    }

    private String downloadFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "download";
        }
        String basename = filename.replace('\\', '/');
        basename = basename.substring(basename.lastIndexOf('/') + 1);
        return basename.replace("\r", "_").replace("\n", "_");
    }
}
