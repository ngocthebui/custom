package com.custom.ngow.shop.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GoogleDriveService {

    private final Drive driveService;

    // Upload file to Google Drive
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            // Determine file MIME type
            String mimeType = file.getContentType();

            // Create file metadata
            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());

            // If folder name is provided, upload to that folder
            if (folderName != null && !folderName.isEmpty()) {
                String folderId = getFolderId(folderName);
                if (folderId == null) {
                    // Create folder if it doesn't exist
                    folderId = createFolder(folderName);
                }
                fileMetadata.setParents(Collections.singletonList(folderId));
            }

            // Create file content
            InputStreamContent mediaContent = new InputStreamContent(
                    mimeType, new ByteArrayInputStream(file.getBytes()));

            // Upload file
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webViewLink")
                    .execute();

            // Make the file publicly accessible
            makeFilePublic(uploadedFile.getId());

            return uploadedFile.getWebViewLink();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Google Drive", e);
        }
    }

    // Create a folder in Google Drive
    public String createFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File folder = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute();

        return folder.getId();
    }

    // Get folder ID by name
    public String getFolderId(String folderName) throws IOException {
        String query = "mimeType='application/vnd.google-apps.folder' and name='" + folderName
                + "' and trashed=false";
        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id)")
                .execute();

        List<File> files = result.getFiles();
        if (files != null && !files.isEmpty()) {
            return files.get(0).getId();
        }
        return null;
    }

    // Make a file publicly accessible
    public void makeFilePublic(String fileId) throws IOException {
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        driveService.permissions().create(fileId, permission).execute();
    }

    // List all files and folders
    public List<File> listFilesAndFolders() throws IOException {
        FileList result = driveService.files().list()
                .setSpaces("drive")
                .setFields("files(id, name, mimeType, webViewLink, thumbnailLink)")
                .execute();

        return result.getFiles();
    }

    // List files by type (image or video)
    public List<File> listFilesByType(String type) throws IOException {
        String query;
        if ("image".equalsIgnoreCase(type)) {
            query = "mimeType contains 'image/' and trashed=false";
        } else if ("video".equalsIgnoreCase(type)) {
            query = "mimeType contains 'video/' and trashed=false";
        } else {
            throw new IllegalArgumentException("Type must be either 'image' or 'video'");
        }

        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name, mimeType, webViewLink, thumbnailLink)")
                .execute();

        return result.getFiles();
    }

    // Get file by ID
    public File getFileById(String fileId) throws IOException {
        return driveService.files().get(fileId)
                .setFields("id, name, mimeType, webViewLink, thumbnailLink")
                .execute();
    }

    // Delete file by ID
    public void deleteFile(String fileId) throws IOException {
        driveService.files().delete(fileId).execute();
    }
}
