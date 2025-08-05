package com.custom.ngow.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.entity.HomeVideo;
import com.custom.ngow.shop.repository.HomeVideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeVideoService {

  private final HomeVideoRepository homeVideoRepository;
  private final MediaStorageService mediaStorageService;

  public HomeVideo createHomeVideo(MultipartFile video, String title, String description) {
    // Lưu video lên S3 và lấy tên file
    String storedVideoFilename = mediaStorageService.storeVideo(video);

    // Lấy URL đầy đủ từ S3
    String videoUrl = mediaStorageService.getVideoUrl(storedVideoFilename);

    HomeVideo homeVideo =
        HomeVideo.builder()
            .title(title)
            .url(videoUrl) // Sử dụng URL đầy đủ từ S3
            .description(description)
            .isActive(true)
            .build();

    return homeVideoRepository.save(homeVideo);
  }

  public HomeVideo getActiveHomeVideo() {
    return homeVideoRepository.findHomeVideoByIsActiveTrue();
  }
}
