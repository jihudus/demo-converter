package com.example.democonverter.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "files")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "filename")
    private String filename;

    @Column(name = "processing")
    private Boolean isProcessing;

    @Column(name = "last_operation")
    private Boolean lastOperationSuccess;
}
