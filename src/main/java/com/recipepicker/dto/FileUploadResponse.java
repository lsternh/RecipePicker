package com.recipepicker.dto;

import java.util.UUID;

public record FileUploadResponse(
        UUID id,
        String filename,
        int lastChunk,
        int totalChunks,
        String status
) {}