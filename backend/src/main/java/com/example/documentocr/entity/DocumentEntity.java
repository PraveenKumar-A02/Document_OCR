package com.example.documentocr.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @Column(name = "language")
    private String language;

    @Column(name = "translated_content", columnDefinition="TEXT")
    private String translatedContent;

    @Column(name = "translated_content_language")
    private String translatedContentLanguage;

    public DocumentEntity() {}

    public DocumentEntity(String originalFilename, Date uploadDate, String content, String language,
                          String translatedContent, String translatedContentLanguage) {
        this.originalFilename = originalFilename;
        this.uploadDate = uploadDate;
        this.content = content;
        this.language = language;
        this.translatedContent = translatedContent;
        this.translatedContentLanguage = translatedContentLanguage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getTranslatedContent() { return translatedContent; }
    public void setTranslatedContent(String translatedContent) { this.translatedContent = translatedContent; }
    public String getTranslatedContentLanguage() { return translatedContentLanguage; }
    public void setTranslatedContentLanguage(String translatedContentLanguage) { this.translatedContentLanguage = translatedContentLanguage; }
}