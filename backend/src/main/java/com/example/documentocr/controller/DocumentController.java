package com.example.documentocr.controller;

import com.example.documentocr.entity.DocumentEntity;
import com.example.documentocr.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${python.script.path}")
    private String pythonScriptPath;

    @Value("${upload.temp.dir}")
    private String tempUploadDir;

    @PostMapping("/process")
    public Map<String, String> processDocument(@RequestParam("file") MultipartFile file,
                                               @RequestParam("sourceLanguage") String sourceLang,
                                               @RequestParam("targetLanguage") String targetLang) {
        try {
            File tempFile = new File(tempUploadDir + File.separator + file.getOriginalFilename());
            file.transferTo(tempFile);

            ProcessBuilder pb = new ProcessBuilder(
                    "python", pythonScriptPath,
                    "--image", tempFile.getAbsolutePath(),
                    "--source", sourceLang,
                    "--target", targetLang
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code " + exitCode + ": " + output.toString());
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> result;
            try {
                result = mapper.readValue(output.toString(), Map.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Python script output: " + output.toString(), e);
            }

            String originalContent = result.get("original");
            String translatedContent = result.get("translated");

            if (originalContent == null || translatedContent == null) {
                throw new RuntimeException("Invalid response from Python script: missing 'original' or 'translated' field");
            }

            DocumentEntity document = new DocumentEntity();
            document.setOriginalFilename(file.getOriginalFilename());
            document.setUploadDate(new Date());
            document.setContent(originalContent);
            document.setLanguage(sourceLang);
            document.setTranslatedContent(translatedContent);
            document.setTranslatedContentLanguage(targetLang);
            documentRepository.save(document);

            Files.deleteIfExists(tempFile.toPath());

            return Map.of(
                    "original", originalContent,
                    "translated", translatedContent
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing document: " + e.getMessage());
        }
    }

    @GetMapping
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    @GetMapping("/{id}")
    public DocumentEntity getDocumentById(@PathVariable Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    @PutMapping("/{id}")
    public DocumentEntity updateDocument(@PathVariable Long id, @RequestBody Map<String, String> json) {
        DocumentEntity document = documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        document.setContent(json.getOrDefault("content", document.getContent()));
        document.setLanguage(json.getOrDefault("language", document.getLanguage()));
        document.setTranslatedContent(json.getOrDefault("translatedContent", document.getTranslatedContent()));
        document.setTranslatedContentLanguage(json.getOrDefault("translatedContentLanguage", document.getTranslatedContentLanguage()));
        return documentRepository.save(document);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
    }
}