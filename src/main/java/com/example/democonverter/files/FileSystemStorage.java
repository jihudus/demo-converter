package com.example.democonverter.files;

import com.example.democonverter.service.FileStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileSystemStorage implements FileStoreService {

    private final FileStorageConfig fileStorageConfig;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) originalFilename = getTimeStamp();
        file.transferTo(new File(fileStorageConfig.getBasePath(), originalFilename));
        return originalFilename;
    }

    private String getTimeStamp() {
        return LocalDateTime.now().toString().replace(':', '-');
    }

    @Override
    public boolean removeFile(String name) {
        return getFileByName(name).delete();
    }

    @Override
    public File getFileByName(String fileName) {
        return new File(fileStorageConfig.getBasePath(), fileName);
    }

    @Override
    public void merge(String filename) {
        File file = new File(fileStorageConfig.getTempPath() + filename);
        file.renameTo(new File(fileStorageConfig.getBasePath() + filename));
    }
}
