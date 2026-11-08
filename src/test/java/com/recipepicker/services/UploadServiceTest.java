package com.recipepicker.services;

import com.recipepicker.dto.SplitFileUploadResponse;
import com.recipepicker.repositories.FileUploadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class UploadServiceTest {

    @Test
    void splitsFileIntoTheRequestedNumberOfOrderedChunks() throws Exception {
        FileUploadRepository uploadRepository = mock(FileUploadRepository.class);
        Path storageDirectory = Files.createTempDirectory("recipe-picker-chunks-");
        UploadService service = new UploadService(uploadRepository, storageDirectory.toString());

        MockMultipartFile file = new MockMultipartFile(
                "file", "example.bin", "application/octet-stream", new byte[10]);

        try {
            SplitFileUploadResponse result = service.splitAndSave(file, 3, "example");

            assertEquals("example.bin", result.filename());
            assertEquals(10, result.fileSize());
            assertEquals(3, result.totalChunks());
            assertEquals("COMPLETE", result.status());
            assertEquals(List.of(4, 4, 2), result.chunks().stream()
                    .map(chunk -> chunk.size())
                    .toList());
            assertEquals(List.of(0, 1, 2), result.chunks().stream()
                    .map(chunk -> chunk.chunkNumber())
                    .toList());
            assertEquals(4, Files.size(Path.of(result.storageDirectory(), "chunk-00000.part")));
        } finally {
            try (var paths = Files.walk(storageDirectory)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                });
            }
        }
    }
}
