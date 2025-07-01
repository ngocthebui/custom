package com.custom.ngow.shop.controller;

import com.custom.ngow.shop.entity.HomeSlide;
import com.custom.ngow.shop.service.HomeSlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/home-slide")
@RequiredArgsConstructor
public class HomeSlideController {

  private final HomeSlideService homeSlideService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<HomeSlide> uploadSlide(
      @RequestParam("video") MultipartFile file,
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("displayOrder") Integer displayOrder) {
    return ResponseEntity.ok(
        homeSlideService.createHomeSlide(file, title, description, displayOrder));
  }

}
