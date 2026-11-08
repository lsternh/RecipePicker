package com.recipepicker.services;

import com.recipepicker.dto.CreateUploadRequest;
import com.recipepicker.dto.FileUploadChunkResponse;
import com.recipepicker.dto.FileUploadResponse;
import com.recipepicker.dto.SplitFileUploadResponse;
import com.recipepicker.entity.FileUploadEntity;
import com.recipepicker.repositories.FileUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {

    private final FileUploadRepository repository;
    private final Path chunkUploadDirectory;

    public UploadService(FileUploadRepository repository,
                         @Value("${file.chunk.upload.dir}") String chunkUploadDirectory) {
        this.repository = repository;
        this.chunkUploadDirectory = Path.of(chunkUploadDirectory);
    }

    public FileUploadResponse createUpload(CreateUploadRequest request) {

        if (request.totalChunks() <= 0) {
            throw new IllegalArgumentException("totalChunks must be greater than zero");
        }


        FileUploadEntity upload = FileUploadEntity.builder()
                .filename(request.filename())
                .description(request.description())
                .body(new byte[0])
                .lastChunk(0)
                .totalChunks(request.totalChunks())
                .status("UPLOADING")
                .build();

        FileUploadEntity saved = repository.save(upload);

        return toResponse(saved);
    }

    public FileUploadResponse appendChunk(UUID uploadId, int chunkNumber, byte[] chunk) {

        FileUploadEntity upload = repository.findById(uploadId)
                .orElseThrow(() -> new RuntimeException(String.valueOf(uploadId)));

        if ("COMPLETE".equals(upload.getStatus())) {
            throw new IllegalStateException(
                    "Upload has already been completed");
        }

        if (chunkNumber < 0 ||
                chunkNumber > upload.getTotalChunks()) {

            throw new IllegalArgumentException(
                    "Invalid chunk number: " + chunkNumber);
        }

        int expectedChunk = upload.getLastChunk() + 1;

        if (chunkNumber != expectedChunk) {
            throw new IllegalArgumentException(
                    "Expected chunk " + expectedChunk +
                            " but received " + chunkNumber);
        }

        byte[] existingBody = upload.getBody();

        byte[] newBody = new byte[
                existingBody.length + chunk.length];

        System.arraycopy(existingBody, 0, newBody, 0, existingBody.length);

        System.arraycopy(chunk, 0, newBody, existingBody.length, chunk.length);

        upload.setBody(newBody);
        upload.setLastChunk(chunkNumber);

        if (chunkNumber == upload.getTotalChunks()) {
            upload.setStatus("COMPLETE");
        }

        FileUploadEntity saved = repository.save(upload);

        return toResponse(saved);
    }

    public FileUploadEntity getCompletedUpload(UUID uploadId) {
        FileUploadEntity upload = repository.findById(uploadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Upload not found: " + uploadId));

        if (!"COMPLETE".equals(upload.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Upload is not complete: " + uploadId);
        }

        return upload;
    }

    /**
     * Splits a complete multipart upload into equally sized (apart from the final) files.
     */
    public SplitFileUploadResponse splitAndSave(MultipartFile file, int chunkCount, String description) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file must not be empty");
        }
        if (chunkCount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "chunkCount must be greater than zero");
        }
        if (chunkCount > file.getSize()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "chunkCount cannot be greater than the number of bytes in the file");
        }

        final byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to read uploaded file", exception);
        }

        UUID uploadId = UUID.randomUUID();
        Path uploadDirectory = chunkUploadDirectory.resolve(uploadId.toString());
        int chunkSize = Math.ceilDiv(fileBytes.length, chunkCount);
        List<FileUploadChunkResponse> chunkResponses = new ArrayList<>(chunkCount);
        try {
            Files.createDirectories(uploadDirectory);
            for (int chunkNumber = 0; chunkNumber < chunkCount; chunkNumber++) {
                int start = chunkNumber * chunkSize;
                int end = Math.min(start + chunkSize, fileBytes.length);
                byte[] chunkBody = new byte[end - start];
                System.arraycopy(fileBytes, start, chunkBody, 0, chunkBody.length);

                String chunkFilename = String.format("chunk-%05d.part", chunkNumber);
                Files.write(uploadDirectory.resolve(chunkFilename), chunkBody);
                chunkResponses.add(new FileUploadChunkResponse(chunkNumber, chunkBody.length, chunkFilename));
            }
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to save file chunks", exception);
        }

        return new SplitFileUploadResponse(
                uploadId,
                file.getOriginalFilename() == null ? "upload" : file.getOriginalFilename(),
                fileBytes.length,
                chunkCount,
                "COMPLETE",
                uploadDirectory.toAbsolutePath().toString(),
                chunkResponses);
    }

    private FileUploadResponse toResponse(FileUploadEntity upload) {
        return new FileUploadResponse(
                upload.getId(),
                upload.getFilename(),
                upload.getLastChunk(),
                upload.getTotalChunks(),
                upload.getStatus());
    }
}
