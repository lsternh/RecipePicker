package com.recipepicker.dto;

public record CreateUploadRequest(
        String filename,
        String description,
        int totalChunks
) {}