package com.recipepicker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "file_upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileUploadEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String filename;

    private String description;

    @Column(nullable = false)
    private byte[] body;

    @Column(nullable = false)
    private int lastChunk;

    @Column(nullable = false)
    private int totalChunks;

    @Column(nullable = false)
    private String status;

}