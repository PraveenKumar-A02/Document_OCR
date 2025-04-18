-- Use this script to initialize the database for the Document OCR Project

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS document_ocr;

-- Use the newly created or existing database
USE document_ocr;

-- Drop the translations table if it exists (no longer needed)
DROP TABLE IF EXISTS translations;

-- Drop the old documents table if it exists to avoid conflicts
DROP TABLE IF EXISTS documents;

-- Create the updated documents table
CREATE TABLE documents (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           original_filename VARCHAR(255) NOT NULL,
                           upload_date DATETIME NOT NULL,
                           content TEXT NOT NULL,
                           language CHAR(3) NOT NULL,
                           translated_content TEXT,
                           translated_content_language CHAR(3),
                           INDEX idx_language (language)
);

-- Ensure the database uses UTF-8 to support multiple languages
ALTER DATABASE document_ocr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Update table to use UTF-8
ALTER TABLE documents CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;