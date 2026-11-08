package com.recipepicker.repositories;

import com.recipepicker.entity.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileUploadRepository
        extends JpaRepository<FileUploadEntity, UUID> {
}