package com.example.democonverter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileStoreService {

    String storeFile(MultipartFile file) throws IOException;

    boolean removeFile(String name);

    File getFileByName(String fileName);

    void merge(String filename);
}
