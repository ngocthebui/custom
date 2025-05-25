package com.custom.ngow.shop.service;

import com.custom.ngow.shop.entity.HomeSlide;
import com.custom.ngow.shop.repository.HomeSlideRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class HomeSlideService {

    private final HomeSlideRepository homeSlideRepository;
    private final MediaStorageService mediaStorageService;

    public HomeSlide createHomeSlide(MultipartFile slide, String title, String description,
            Integer displayOrder) {
        String storedSlideUrl = mediaStorageService.storeImage(slide);

        HomeSlide homeSlide = HomeSlide.builder()
                .title(title)
                .url("/api/media/images/" + storedSlideUrl)
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
