package com.example.democonverter.data;

import lombok.Data;

import java.util.UUID;

@Data
public class FileDTO extends AbstractDTO {

    private final UUID id;

    private final String filename;

    private final Boolean processing;

    private final Boolean processingSuccess;

    public FileDTO(FileEntity fileEntity) {
        id = fileEntity.getId();
        filename = fileEntity.getFilename();
        processing = fileEntity.getIsProcessing();
        processingSuccess = fileEntity.getLastOperationSuccess();
    }
}
