package com.custom.ngow.shop.controller;

import com.custom.ngow.shop.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/drive")
@RequiredArgsConstructor
public class GoogleDriveController {

    private final GoogleDriveService driveService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderName", required = false) String folderName) {
        try {
            String fileUrl = driveService.uploadFile(file, folderName);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> listFiles() {
        try {
            List<File> files = driveService.listFilesAndFolders();
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<List<File>> listFilesByType(@PathVariable String type) {
        try {
            List<File> files = driveService.listFilesByType(type);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFileById(@PathVariable String fileId) {
        try {
            File file = driveService.getFileById(fileId);
            return ResponseEntity.ok(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            driveService.deleteFile(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

    @PostMapping("/createFolder")
    public ResponseEntity<String> createFolder(@RequestParam String folderName) {
        try {
            String folderId = driveService.createFolder(folderName);
            return ResponseEntity.ok("Folder created with ID: " + folderId);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to create folder: " + e.getMessage());
        }
    }
}
