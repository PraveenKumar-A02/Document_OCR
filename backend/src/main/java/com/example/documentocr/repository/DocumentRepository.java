package com.example.documentocr.repository;

import com.example.documentocr.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    //CRUD OPERATIONS
}