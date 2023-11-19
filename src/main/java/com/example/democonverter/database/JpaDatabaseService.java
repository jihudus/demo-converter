package com.example.democonverter.database;

import com.example.democonverter.data.FileEntity;
import com.example.democonverter.service.DBService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JpaDatabaseService implements DBService {

    private final FileEntityRepository fileEntityRepository;

    @Override
    public void persistFile(FileEntity fileEntity) {
        fileEntityRepository.save(fileEntity);
    }

    @Override
    public void setInProgress(UUID fileId, Boolean b) {
        FileEntity fileEntity = getFileEntity(fileId);
        fileEntity.setIsProcessing(b);
        fileEntityRepository.save(fileEntity);
    }

    @Override
    public FileEntity getFileEntity(UUID fileId) {
        return fileEntityRepository.getReferenceById(fileId);
    }

    @Override
    public boolean delete(UUID fileId) {
        fileEntityRepository.deleteById(fileId);
        return true;
    }
}
