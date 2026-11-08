package com.recipepicker.dto;

public record FileUploadChunkResponse(
        int chunkNumber,
        int size,
        String filename
) {
}
