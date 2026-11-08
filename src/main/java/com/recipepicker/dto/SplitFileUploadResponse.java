package com.recipepicker.dto;

import java.util.List;
import java.util.UUID;

public record SplitFileUploadResponse(
        UUID id,
        String filename,
        long fileSize,
        int totalChunks,
        String status,
        String storageDirectory,
        List<FileUploadChunkResponse> chunks
) {
}
