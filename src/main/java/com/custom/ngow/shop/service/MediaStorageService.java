package com.custom.ngow.shop.service;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.image-folder}")
    private String imageFolder;

    @Value("${aws.s3.video-folder}")
    private String videoFolder;

    @Value("${aws.s3.cdn-url}")
    private String cdnUrl;

    /**
     * Lưu trữ ảnh lên S3
     */
    public String storeImage(MultipartFile file) {
        return storeFileToS3(file, imageFolder, true);
    }

    /**
     * Lưu trữ video lên S3
     */
    public String storeVideo(MultipartFile file) {
        return storeFileToS3(file, videoFolder, false);
    }

    private String storeFileToS3(MultipartFile file, String folder, boolean isImage) {
        // Kiểm tra file không rỗng
        if (file.isEmpty()) {
            throw new RuntimeException("Không thể lưu trữ tệp rỗng");
        }

        // Lấy tên file gốc và làm sạch
        String originalFilename = StringUtils.cleanPath(
            Objects.requireNonNull(file.getOriginalFilename()));

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

            // Tạo key cho S3 (bao gồm folder)
            String s3Key = folder + newFilename;

            // Xác định content type
            String contentType = isImage ?
                getImageContentType(originalFilename) :
                getVideoContentType(originalFilename);

            // Tạo request để upload lên S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            // Upload file lên S3
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Đã upload {} thành công lên S3 với key: {}",
                    isImage ? "ảnh" : "video", s3Key);

            return newFilename;

        } catch (IOException ex) {
            throw new RuntimeException("Không thể upload tệp " + originalFilename + " lên S3", ex);
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
     * Lấy URL đầy đủ của ảnh từ S3
     */
    public String getImageUrl(String filename) {
        return cdnUrl + imageFolder + filename;
    }

    /**
     * Lấy URL đầy đủ của video từ S3
     */
    public String getVideoUrl(String filename) {
        return cdnUrl + videoFolder + filename;
    }

    /**
     * Xác định content type cho file ảnh
     */
    private String getImageContentType(String filename) {
        String lowercaseFilename = filename.toLowerCase();
        if (lowercaseFilename.endsWith(".jpg") || lowercaseFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowercaseFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowercaseFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowercaseFilename.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Xác định content type cho file video
     */
    private String getVideoContentType(String filename) {
        String lowercaseFilename = filename.toLowerCase();
        if (lowercaseFilename.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowercaseFilename.endsWith(".webm")) {
            return "video/webm";
        } else if (lowercaseFilename.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (lowercaseFilename.endsWith(".mov")) {
            return "video/quicktime";
        } else if (lowercaseFilename.endsWith(".wmv")) {
            return "video/x-ms-wmv";
        } else if (lowercaseFilename.endsWith(".mkv")) {
            return "video/x-matroska";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Xác định MediaType cho file ảnh (giữ lại để backward compatibility)
     */
    public MediaType getImageMediaType(String filename) {
        String lowercaseFilename = filename.toLowerCase();
        if (lowercaseFilename.endsWith(".jpg") || lowercaseFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (lowercaseFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowercaseFilename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (lowercaseFilename.endsWith(".webp")) {
            return MediaType.valueOf("image/webp");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * Xác định MediaType cho file video (giữ lại để backward compatibility)
     */
    public MediaType getVideoMediaType(String filename) {
        String lowercaseFilename = filename.toLowerCase();
        if (lowercaseFilename.endsWith(".mp4")) {
            return MediaType.valueOf("video/mp4");
        } else if (lowercaseFilename.endsWith(".webm")) {
            return MediaType.valueOf("video/webm");
        } else if (lowercaseFilename.endsWith(".avi")) {
            return MediaType.valueOf("video/x-msvideo");
        } else if (lowercaseFilename.endsWith(".mov")) {
            return MediaType.valueOf("video/quicktime");
        } else if (lowercaseFilename.endsWith(".wmv")) {
            return MediaType.valueOf("video/x-ms-wmv");
        } else if (lowercaseFilename.endsWith(".mkv")) {
            return MediaType.valueOf("video/x-matroska");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
