package com.example.democonverter.service;

import com.example.democonverter.data.FileEntity;

import java.util.UUID;

public interface DBService {
    
    void persistFile(FileEntity fileEntity);

    void setInProgress(UUID fileId, Boolean b);

    FileEntity getFileEntity(UUID fileId);

    boolean delete(UUID fileId);
}
