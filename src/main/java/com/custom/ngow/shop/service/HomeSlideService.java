package com.custom.ngow.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.entity.HomeSlide;
import com.custom.ngow.shop.repository.HomeSlideRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeSlideService {

  private final HomeSlideRepository homeSlideRepository;
  private final MediaStorageService mediaStorageService;

  public HomeSlide createHomeSlide(
      MultipartFile slide, String title, String description, Integer displayOrder) {
    // Lưu ảnh lên S3 và lấy tên file
    String storedSlideFilename = mediaStorageService.storeImage(slide);

    // Lấy URL đầy đủ từ S3
    String slideUrl = mediaStorageService.getImageUrl(storedSlideFilename);

    HomeSlide homeSlide =
        HomeSlide.builder()
            .title(title)
            .url(slideUrl) // Sử dụng URL đầy đủ từ S3
            .description(description)
            .isActive(true)
            .displayOrder(displayOrder)
            .build();

    return homeSlideRepository.save(homeSlide);
  }

  public List<HomeSlide> getActiveHomeSlides() {
    return homeSlideRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc();
  }
}
