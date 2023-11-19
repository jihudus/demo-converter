package com.example.democonverter.database;

import com.example.democonverter.data.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileEntityRepository extends JpaRepository<FileEntity, UUID> {
}