package com.custom.ngow.shop.controller;

import com.custom.ngow.shop.entity.HomeVideo;
import com.custom.ngow.shop.service.HomeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/home-video")
@RequiredArgsConstructor
public class HomeVideoController {

  private final HomeVideoService homeVideoService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<HomeVideo> uploadVideo(
      @RequestParam("video") MultipartFile file,
      @RequestParam("title") String title,
      @RequestParam("description") String description) {
    return ResponseEntity.ok(homeVideoService.createHomeVideo(file, title, description));
  }
}
