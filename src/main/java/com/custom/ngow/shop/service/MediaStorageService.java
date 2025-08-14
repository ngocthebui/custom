package com.custom.ngow.shop.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaStorageService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  @Value("${aws.s3.cdn-url}")
  private String cdnUrl;

  @Value("${aws.s3.public-url}")
  private String publicUrl;

  /** Lưu trữ file lên S3 với đường dẫn folder tùy chỉnh */
  public String storeFile(MultipartFile file, String folderPath, FileType fileType) {
    return storeFileToS3(file, folderPath, fileType);
  }

  /** Lưu trữ ảnh lên S3 với folder mặc định */
  public String storeImage(MultipartFile file, String folderPath) {
    return storeFileToS3(file, folderPath, FileType.IMAGE);
  }

  /** Lưu trữ video lên S3 với folder mặc định */
  public String storeVideo(MultipartFile file, String folderPath) {
    return storeFileToS3(file, folderPath, FileType.VIDEO);
  }

  private String storeFileToS3(MultipartFile file, String folderPath, FileType fileType) {
    // Kiểm tra file không rỗng
    if (file.isEmpty()) {
      throw new RuntimeException("Không thể lưu trữ tệp rỗng");
    }

    // Lấy tên file gốc và làm sạch
    String originalFilename =
        StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    // Kiểm tra định dạng file
    validateFileType(originalFilename, fileType);

    try {
      // Chuẩn hóa đường dẫn folder
      String normalizedFolderPath = normalizeFolderPath(folderPath);

      // Kiểm tra và tạo folder nếu chưa tồn tại
      ensureFolderExists(normalizedFolderPath);

      // Tạo tên file duy nhất để tránh xung đột
      String fileExtension = getFileExtension(originalFilename);
      String newFilename = UUID.randomUUID() + fileExtension;

      // Tạo key cho S3 (bao gồm folder)
      String s3Key = normalizedFolderPath + newFilename;

      // Xác định content type
      String contentType = getContentType(originalFilename, fileType);

      // Tạo request để upload lên S3
      PutObjectRequest putObjectRequest =
          PutObjectRequest.builder()
              .bucket(bucketName)
              .key(s3Key)
              .contentType(contentType)
              .contentLength(file.getSize())
              .build();

      // Upload file lên S3
      s3Client.putObject(
          putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      log.info("Đã upload {} thành công lên S3 với key: {}", fileType.getDisplayName(), s3Key);

      return newFilename;

    } catch (IOException ex) {
      throw new RuntimeException("Không thể upload tệp " + originalFilename + " lên S3", ex);
    }
  }

  /** Chuẩn hóa đường dẫn folder */
  private String normalizeFolderPath(String folderPath) {
    if (folderPath == null || folderPath.trim().isEmpty()) {
      return "";
    }

    // Loại bỏ ký tự đầu và cuối không cần thiết
    String normalized = folderPath.trim();

    // Thêm dấu "/" ở cuối nếu chưa có
    if (!normalized.endsWith("/")) {
      normalized += "/";
    }

    // Loại bỏ dấu "/" ở đầu nếu có
    if (normalized.startsWith("/")) {
      normalized = normalized.substring(1);
    }

    return normalized;
  }

  /** Kiểm tra và tạo folder nếu chưa tồn tại */
  private void ensureFolderExists(String folderPath) {
    if (folderPath == null || folderPath.isEmpty()) {
      return;
    }

    try {
      // Kiểm tra xem folder đã tồn tại chưa bằng cách list objects
      ListObjectsV2Request listRequest =
          ListObjectsV2Request.builder().bucket(bucketName).prefix(folderPath).maxKeys(1).build();

      ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

      // Nếu chưa có object nào trong folder, tạo folder marker
      if (listResponse.contents().isEmpty()) {
        PutObjectRequest putObjectRequest =
            PutObjectRequest.builder().bucket(bucketName).key(folderPath).contentLength(0L).build();

        s3Client.putObject(putObjectRequest, RequestBody.empty());
        log.info("Đã tạo folder mới: {}", folderPath);
      }

    } catch (Exception ex) {
      log.warn("Không thể kiểm tra/tạo folder {}: {}", folderPath, ex.getMessage());
      // Không throw exception vì folder có thể được tạo tự động khi upload file
    }
  }

  private void validateFileType(String filename, FileType fileType) {
    switch (fileType) {
      case IMAGE:
        validateImageFileType(filename);
        break;
      case VIDEO:
        validateVideoFileType(filename);
        break;
      default:
        throw new RuntimeException("Loại file không được hỗ trợ");
    }
  }

  private void validateImageFileType(String filename) {
    String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    if (!hasValidExtension(filename, allowedExtensions)) {
      throw new RuntimeException("Chỉ hỗ trợ tệp ảnh có định dạng JPG, JPEG, PNG, GIF hoặc WEBP");
    }
  }

  private void validateVideoFileType(String filename) {
    String[] allowedExtensions = {".mp4", ".avi", ".mov", ".wmv", ".mkv", ".webm"};
    if (!hasValidExtension(filename, allowedExtensions)) {
      throw new RuntimeException(
          "Chỉ hỗ trợ tệp video có định dạng MP4, AVI, MOV, WMV, MKV hoặc WEBM");
    }
  }

  private boolean hasValidExtension(String filename, String[] allowedExtensions) {
    String lowercaseFilename = filename.toLowerCase();
    return Arrays.stream(allowedExtensions).anyMatch(lowercaseFilename::endsWith);
  }

  private String getFileExtension(String filename) {
    int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex > 0) {
      return filename.substring(lastDotIndex);
    }
    return "";
  }

  /** Lấy URL đầy đủ của file từ S3 */
  public String getFileUrl(String folderPath, String filename) {
    String normalizedFolderPath = normalizeFolderPath(folderPath);
    return publicUrl + "/" + bucketName + "/" + normalizedFolderPath + filename;
  }

  /** Xóa file khỏi S3 */
  public boolean deleteFile(String folderPath, String filename) {
    try {
      String normalizedFolderPath = normalizeFolderPath(folderPath);
      String s3Key = normalizedFolderPath + filename;

      DeleteObjectRequest deleteObjectRequest =
          DeleteObjectRequest.builder().bucket(bucketName).key(s3Key).build();

      s3Client.deleteObject(deleteObjectRequest);
      log.info("Delete the file successfully: {}", s3Key);
      return true;

    } catch (Exception ex) {
      log.error("Error when deleting the file {}/{}: {}", folderPath, filename, ex.getMessage());
      return false;
    }
  }

  /** Xóa file bằng URL đầy đủ */
  public void deleteFileByUrl(String fileUrl) {
    try {
      // Extract key từ URL
      String s3Key = extractS3KeyFromUrl(fileUrl);
      if (s3Key == null) {
        log.warn("Can not extract s3 key from URL: {}", fileUrl);
        return;
      }

      DeleteObjectRequest deleteObjectRequest =
          DeleteObjectRequest.builder().bucket(bucketName).key(s3Key).build();

      s3Client.deleteObject(deleteObjectRequest);
      log.info("Has deleted the file successfully with the URL: {}", fileUrl);

    } catch (Exception ex) {
      log.error("Error when deleting the file with the URL {}: {}", fileUrl, ex.getMessage());
    }
  }

  /** Extract S3 key từ URL */
  private String extractS3KeyFromUrl(String fileUrl) {
    try {
      // URL format: {publicUrl}/{bucketName}/{s3Key}
      String baseUrl = publicUrl + "/" + bucketName + "/";

      if (fileUrl.startsWith(baseUrl)) {
        return fileUrl.substring(baseUrl.length());
      }

      // CDN URL
      if (cdnUrl != null && !cdnUrl.isEmpty() && fileUrl.startsWith(cdnUrl)) {
        return fileUrl.substring(cdnUrl.length() + 1); // +1 để bỏ dấu "/"
      }

      return null;

    } catch (Exception ex) {
      log.error("Error when Extract S3 Key from URL: {}", ex.getMessage());
      return null;
    }
  }

  /** Xác định content type cho file */
  private String getContentType(String filename, FileType fileType) {
    switch (fileType) {
      case IMAGE:
        return getImageContentType(filename);
      case VIDEO:
        return getVideoContentType(filename);
      default:
        return "application/octet-stream";
    }
  }

  /** Xác định content type cho file ảnh */
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

  /** Xác định content type cho file video */
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

  /** Enum để định nghĩa loại file */
  @Getter
  public enum FileType {
    IMAGE("ảnh"),
    VIDEO("video");

    private final String displayName;

    FileType(String displayName) {
      this.displayName = displayName;
    }
  }
}
