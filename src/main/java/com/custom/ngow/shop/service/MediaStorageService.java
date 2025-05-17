package com.custom.ngow.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class MediaStorageService {

    private final Path imageStorageLocation;
    private final Path videoStorageLocation;

    public MediaStorageService(
            @Value("${media.image-upload-dir:./uploads/images}") String imageUploadDir,
            @Value("${media.video-upload-dir:./uploads/videos}") String videoUploadDir) {

        this.imageStorageLocation = Paths.get(imageUploadDir)
                .toAbsolutePath().normalize();
        this.videoStorageLocation = Paths.get(videoUploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
            Files.createDirectories(this.videoStorageLocation);
            log.info("Đã tạo thư mục lưu trữ ảnh tại: {}", this.imageStorageLocation);
            log.info("Đã tạo thư mục lưu trữ video tại: {}", this.videoStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Không thể tạo thư mục để lưu trữ tệp media.", ex);
        }
    }

    /**
     * Lưu trữ ảnh vào thư mục local
     */
    public String storeImage(MultipartFile file) {
        return storeFile(file, this.imageStorageLocation, true);
    }

    /**
     * Lưu trữ video vào thư mục local
     */
    public String storeVideo(MultipartFile file) {
        return storeFile(file, this.videoStorageLocation, false);
    }

    private String storeFile(MultipartFile file, Path storageLocation, boolean isImage) {
        // Kiểm tra file không rỗng
        if (file.isEmpty()) {
            throw new RuntimeException("Không thể lưu trữ tệp rỗng");
        }

        // Lấy tên file gốc và làm sạch
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // Kiểm tra định dạng file
        if (isImage) {
            validateImageFileType(originalFilename);
        } else {
            validateVideoFileType(originalFilename);
        }

        try {
            // Tạo tên file duy nhất để tránh xung đột
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID() + fileExtension;

            // Tạo đường dẫn đầy đủ để lưu file
            Path targetLocation = storageLocation.resolve(newFilename);

            // Sao chép file vào vị trí đích
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("Đã lưu tệp {} thành công tại: {}", isImage ? "ảnh" : "video", targetLocation);
            return newFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu tệp " + originalFilename, ex);
        }
    }

    private void validateImageFileType(String filename) {
        // Kiểm tra định dạng ảnh hợp lệ
        if (!filename.toLowerCase().endsWith(".jpg") &&
                !filename.toLowerCase().endsWith(".jpeg") &&
                !filename.toLowerCase().endsWith(".png") &&
                !filename.toLowerCase().endsWith(".gif") &&
                !filename.toLowerCase().endsWith(".webp")) {
            throw new RuntimeException(
                    "Chỉ hỗ trợ tệp ảnh có định dạng JPG, JPEG, PNG, GIF hoặc WEBP");
        }
    }

    private void validateVideoFileType(String filename) {
        // Kiểm tra định dạng video hợp lệ
        if (!filename.toLowerCase().endsWith(".mp4") &&
                !filename.toLowerCase().endsWith(".avi") &&
                !filename.toLowerCase().endsWith(".mov") &&
                !filename.toLowerCase().endsWith(".wmv") &&
                !filename.toLowerCase().endsWith(".mkv") &&
                !filename.toLowerCase().endsWith(".webm")) {
            throw new RuntimeException(
                    "Chỉ hỗ trợ tệp video có định dạng MP4, AVI, MOV, WMV, MKV hoặc WEBM");
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }

    /**
     * Lấy đường dẫn tệp ảnh theo tên tệp
     */
    public Path getImagePath(String filename) {
        return this.imageStorageLocation.resolve(filename);
    }

    /**
     * Lấy đường dẫn tệp video theo tên tệp
     */
    public Path getVideoPath(String filename) {
        return this.videoStorageLocation.resolve(filename);
    }
}
