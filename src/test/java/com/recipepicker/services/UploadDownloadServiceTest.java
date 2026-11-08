package com.recipepicker.services;

import com.recipepicker.entity.FileUploadEntity;
import com.recipepicker.repositories.FileUploadRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UploadDownloadServiceTest {

    @Test
    void returnsOnlyACompletedUpload() {
        FileUploadRepository repository = mock(FileUploadRepository.class);
        UploadService service = new UploadService(repository, "build/test-chunks");
        UUID uploadId = UUID.randomUUID();
        FileUploadEntity upload = FileUploadEntity.builder()
                .id(uploadId)
                .filename("notes.txt")
                .body(new byte[]{1, 2, 3})
                .lastChunk(0)
                .totalChunks(1)
                .status("COMPLETE")
                .build();
        when(repository.findById(uploadId)).thenReturn(Optional.of(upload));

        assertSame(upload, service.getCompletedUpload(uploadId));
    }

    @Test
    void rejectsAnUploadThatIsStillInProgress() {
        FileUploadRepository repository = mock(FileUploadRepository.class);
        UploadService service = new UploadService(repository, "build/test-chunks");
        UUID uploadId = UUID.randomUUID();
        FileUploadEntity upload = FileUploadEntity.builder()
                .id(uploadId)
                .filename("notes.txt")
                .body(new byte[0])
                .lastChunk(-1)
                .totalChunks(1)
                .status("UPLOADING")
                .build();
        when(repository.findById(uploadId)).thenReturn(Optional.of(upload));

        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.getCompletedUpload(uploadId));
    }
}
