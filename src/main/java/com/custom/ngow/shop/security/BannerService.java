package com.custom.ngow.shop.security;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.BannerDto;
import com.custom.ngow.shop.entity.Banner;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.BannerRepository;
import com.custom.ngow.shop.service.MediaStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BannerService {

  private final BannerRepository bannerRepository;
  private final ModelMapper modelMapper;
  private final MediaStorageService mediaStorageService;
  private final MessageUtil messageUtil;

  /**
   * Retrieves all suitable banners that are active and either always visible or valid within the
   * current date range.
   *
   * @return list of suitable banners
   */
  public List<Banner> getAllSuitableBanner() {
    return bannerRepository.findAllSuitableBanner(LocalDateTime.now());
  }

  public Banner getBannerById(long id) {
    return bannerRepository
        .findById(id)
        .orElseThrow(
            () ->
                new CustomException(
                    messageUtil, "", new String[] {"Banner id: " + id}, "error.notExist"));
  }

  public Page<Banner> getAllBanners(int page, int size, String sortBy, String dir) {
    Sort.Direction direction =
        dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    return bannerRepository.findAll(pageable);
  }

  public void registerBanner(Banner banner, BannerDto dto) {
    if (!dto.getIsAlwaysView() && (dto.getStartDate() == null || dto.getEndDate() == null)) {
      throw new CustomException(messageUtil, "", new String[] {"Time"}, "error.required");
    }

    modelMapper.map(dto, banner);

    if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
      // create folder for product image: banners/images/
      String folderPath = "banners/images/";
      uploadBannerImage(banner, folderPath, dto.getImageFile());
    }

    bannerRepository.save(banner);
  }

  private void uploadBannerImage(Banner banner, String folderPath, MultipartFile file) {
    // save image
    String filename = mediaStorageService.storeImage(file, folderPath);

    // Get the full URL of the image
    String imageUrl = mediaStorageService.getFileUrl(folderPath, filename);

    // delete old image
    if (banner.getImageUrl() != null && !banner.getImageUrl().isEmpty()) {
      mediaStorageService.deleteFileByUrl(banner.getImageUrl());
      log.info("Product image: {} deleted old photos", banner.getId());
    }

    banner.setImageUrl(imageUrl);
  }

  public BannerDto getBannerDtoById(long id) {
    Banner banner = getBannerById(id);

    return modelMapper.map(banner, BannerDto.class);
  }

  public long countAllBanner() {
    return bannerRepository.count();
  }

  public long countByIsActive(Boolean isActive) {
    return bannerRepository.countByIsActive(isActive);
  }
}
