package com.custom.ngow.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.ProductStatus;
import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.dto.ProductImageListDto;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductImage;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.ProductImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageService {

  private final ProductImageRepository productImageRepository;
  private final MessageUtil messageUtil;
  private final ProductService productService;
  private final ProductColorService productColorService;
  private final ProductSizeService productSizeService;
  private final MediaStorageService mediaStorageService;

  public List<ProductImageDto> getImagesByProductId(Long productId) {
    return productImageRepository.findByProductId(productId);
  }

  public ProductImage getImageById(Long productImageId) {
    return productImageRepository
        .findById(productImageId)
        .orElseThrow(
            () ->
                new CustomException(
                    messageUtil,
                    "",
                    new String[] {"Image id: " + productImageId},
                    "error.notExist"));
  }

  @Transactional
  public void saveImagesForProduct(ProductImageListDto imageListDto) {
    Product product = productService.getProductById(imageListDto.getProductId());
    product.getImages().clear();

    imageListDto
        .getImages()
        .forEach(
            image -> {
              ProductImage productImage = new ProductImage();

              // new image
              if (image.getId() == null) {
                if (image.getImage() == null || image.getImage().isEmpty()) {
                  throw new CustomException(
                      messageUtil, "", new String[] {"File"}, "error.required");
                }
              } else { // update image
                productImage = getImageById(image.getId());
              }

              productImage.setProduct(product);

              // color
              ProductColor productColor = productColorService.getByColorId(image.getColorId());
              productImage.setColor(productColor);

              // size
              ProductSize productSize = productSizeService.getBySizeId(image.getSizeId());
              productImage.setSize(productSize);

              productImage.setSortOrder(image.getSortOrder());
              productImage.setIsMain(image.getIsMain());

              // upload image
              if (image.getImage() != null && !image.getImage().isEmpty()) {
                // create folder for product image: products/images/{sku}/
                String folderPath = "products/images/" + product.getSku() + "/";
                uploadProductImage(productImage, folderPath, image.getImage());
              }

              product.getImages().add(productImage);
            });

    product.setStatus(ProductStatus.ACTIVE);
    productService.save(product);
  }

  private void uploadProductImage(
      ProductImage productImage, String folderPath, MultipartFile file) {
    // save image
    String filename = mediaStorageService.storeImage(file, folderPath);

    // Get the full URL of the image
    String imageUrl = mediaStorageService.getFileUrl(folderPath, filename);

    // delete old image
    if (productImage.getImageUrl() != null && !productImage.getImageUrl().isEmpty()) {
      mediaStorageService.deleteFileByUrl(productImage.getImageUrl());
      log.info("Product image: {} deleted old photos", productImage.getId());
    }

    productImage.setImageUrl(imageUrl);
  }
}
