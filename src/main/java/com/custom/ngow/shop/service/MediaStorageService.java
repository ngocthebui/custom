package com.custom.ngow.shop.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.constant.FileType;
import com.custom.ngow.shop.exception.CustomException;

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

  /** Store the file to S3 with custom folder path */
  public String storeFile(MultipartFile file, String folderPath, FileType fileType) {
    return storeFileToS3(file, folderPath, fileType);
  }

  /** Store photos on S3 with default folder */
  public String storeImage(MultipartFile file, String folderPath) {
    return storeFileToS3(file, folderPath, FileType.IMAGE);
  }

  /** Store video to S3 with default folder */
  public String storeVideo(MultipartFile file, String folderPath) {
    return storeFileToS3(file, folderPath, FileType.VIDEO);
  }

  private String storeFileToS3(MultipartFile file, String folderPath, FileType fileType) {
    // Check the file is not empty
    if (file.isEmpty()) {
      throw new CustomException("Can not store empty files");
    }

    // Take the name of the original file and clean
    String originalFilename =
        StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    // Check file format
    validateFileType(originalFilename, fileType);

    try {
      // Standardize the folder path
      String normalizedFolderPath = normalizeFolderPath(folderPath);

      // Check and create folders if not existed
      ensureFolderExists(normalizedFolderPath);

      // Create a single file name to avoid conflict
      String fileExtension = getFileExtension(originalFilename);
      String newFilename = UUID.randomUUID() + fileExtension;

      // Create key for S3 (including folder)
      String s3Key = normalizedFolderPath + newFilename;

      // Determine Content Type
      String contentType = getContentType(originalFilename, fileType);

      // Create a request to upload to S3
      PutObjectRequest putObjectRequest =
          PutObjectRequest.builder()
              .bucket(bucketName)
              .key(s3Key)
              .contentType(contentType)
              .contentLength(file.getSize())
              .build();

      // Upload files to S3
      s3Client.putObject(
          putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      log.info("Uploaded {} Success to S3 with key: {}", fileType.getDisplayName(), s3Key);

      return newFilename;

    } catch (IOException ex) {
      log.error("Error while storing file {}", originalFilename, ex);
      throw new RuntimeException("Unable to upload the file " + originalFilename + " to S3", ex);
    }
  }

  /** Standardize the folder path */
  private String normalizeFolderPath(String folderPath) {
    if (folderPath == null || folderPath.trim().isEmpty()) {
      return "";
    }

    // Eliminate the first and last characters
    String normalized = folderPath.trim();

    // Add the "" at the end if not
    if (!normalized.endsWith("/")) {
      normalized += "/";
    }

    // Eliminate "" at the top if any
    if (normalized.startsWith("/")) {
      normalized = normalized.substring(1);
    }

    return normalized;
  }

  /** Check and create folders if not existed */
  private void ensureFolderExists(String folderPath) {
    if (folderPath == null || folderPath.isEmpty()) {
      return;
    }

    try {
      // Check that the folder has existed by isolation of objects
      ListObjectsV2Request listRequest =
          ListObjectsV2Request.builder().bucket(bucketName).prefix(folderPath).maxKeys(1).build();

      ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

      // If there is no object in the folder, create folder markers
      if (listResponse.contents().isEmpty()) {
        PutObjectRequest putObjectRequest =
            PutObjectRequest.builder().bucket(bucketName).key(folderPath).contentLength(0L).build();

        s3Client.putObject(putObjectRequest, RequestBody.empty());
        log.info("Created a new folder: {}", folderPath);
      }

    } catch (Exception ex) {
      log.warn("Unable to check the folder {}: {}", folderPath, ex.getMessage());
      // No throw exception because folders can be automatically created when uploading files
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
        throw new CustomException("File type is not supported");
    }
  }

  private void validateImageFileType(String filename) {
    String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    if (!hasValidExtension(filename, allowedExtensions)) {
      throw new CustomException(
          "Only support image files with JPG, JPEG, PNG, GIF or Webp formats");
    }
  }

  private void validateVideoFileType(String filename) {
    String[] allowedExtensions = {".mp4", ".avi", ".mov", ".wmv", ".mkv", ".webm"};
    if (!hasValidExtension(filename, allowedExtensions)) {
      throw new CustomException(
          "Only support video files with format MP4, AVI, MOV, WMV, MKV or Webm");
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

  /** Get the full URL of the file from S3 */
  public String getFileUrl(String folderPath, String filename) {
    String normalizedFolderPath = normalizeFolderPath(folderPath);
    return publicUrl + "/" + bucketName + "/" + normalizedFolderPath + filename;
  }

  /** Delete files from S3 */
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

  /** Delete the file with full URL */
  public void deleteFileByUrl(String fileUrl) {
    try {
      // Extract key from URL
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

  /** Extract S3 Key from URL */
  private String extractS3KeyFromUrl(String fileUrl) {
    try {
      // URL format: {publicUrl}/{bucketName}/{s3Key}
      String baseUrl = publicUrl + "/" + bucketName + "/";

      if (fileUrl.startsWith(baseUrl)) {
        return fileUrl.substring(baseUrl.length());
      }

      // CDN URL
      if (cdnUrl != null && !cdnUrl.isEmpty() && fileUrl.startsWith(cdnUrl)) {
        return fileUrl.substring(cdnUrl.length() + 1); // +1 to remove the "/"
      }

      return null;

    } catch (Exception ex) {
      log.error("Error when Extract S3 Key from URL: {}", ex.getMessage());
      return null;
    }
  }

  /** Determine the content type for the file */
  private String getContentType(String filename, FileType fileType) {
    return switch (fileType) {
      case IMAGE -> getImageContentType(filename);
      case VIDEO -> getVideoContentType(filename);
      default -> "application/octet-stream";
    };
  }

  /** Determine the content type for the image file */
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

  /** Determine content type for video files */
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
}
