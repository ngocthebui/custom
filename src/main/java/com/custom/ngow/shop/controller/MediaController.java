package com.custom.ngow.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.service.MediaStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

  private final MediaStorageService mediaStorageService;

  @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, String>> uploadImage(
      @RequestParam("image") MultipartFile file) {
    String filename = mediaStorageService.storeImage(file);

    Map<String, String> response = new HashMap<>();
    response.put("filename", filename);
    response.put("message", "Tải lên ảnh thành công");

    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, String>> uploadVideo(
      @RequestParam("video") MultipartFile file) {
    String filename = mediaStorageService.storeVideo(file);

    Map<String, String> response = new HashMap<>();
    response.put("filename", filename);
    response.put("message", "Tải lên video thành công");

    return ResponseEntity.ok(response);
  }

  @GetMapping("/images/{filename}")
  public ResponseEntity<String> getImageUrl(@PathVariable String filename) {
    String imageUrl = mediaStorageService.getImageUrl(filename);
    return ResponseEntity.ok(imageUrl);
  }

  @GetMapping("/videos/{filename}")
  public ResponseEntity<String> getVideoUrl(@PathVariable String filename) {
    String videoUrl = mediaStorageService.getVideoUrl(filename);
    return ResponseEntity.ok(videoUrl);
  }
}
