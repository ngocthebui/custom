package com.custom.ngow.shop.service;

import com.custom.ngow.shop.entity.HomeVideo;
import com.custom.ngow.shop.repository.HomeVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class HomeVideoService {

    private final HomeVideoRepository homeVideoRepository;
    private final MediaStorageService mediaStorageService;

    public HomeVideo createHomeVideo(MultipartFile video, String title, String description) {
        String storedVideoUrl = mediaStorageService.storeVideo(video);

        HomeVideo homeVideo = HomeVideo.builder()
                .title(title)
                .url("/api/media/videos/" + storedVideoUrl)
                .description(description)
                .isActive(true)
                .build();

        return homeVideoRepository.save(homeVideo);
    }

    public HomeVideo getActiveHomeVideo() {
        return homeVideoRepository.findHomeVideoByIsActiveTrue();
    }
}
