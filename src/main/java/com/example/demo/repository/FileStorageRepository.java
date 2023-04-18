package com.example.demo.repository;

import com.example.demo.enumeration.FileStorageStatus;
import com.example.demo.model.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage,Long> {

    FileStorage findByHashId(String id);

    List<FileStorage> findAllByFileStorageStatus(FileStorageStatus status);
}
