package com.custom.ngow.shop.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.constant.ProductStatus;
import com.custom.ngow.shop.dto.ProductColorDto;
import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.dto.ProductListDto;
import com.custom.ngow.shop.dto.ProductRegistration;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductImage;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.ProductColorRepository;
import com.custom.ngow.shop.repository.ProductImageRepository;
import com.custom.ngow.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductImageRepository productImageRepository;
  private final ProductColorRepository productColorRepository;
  private final CategoryService categoryService;
  private final MessageUtil messageUtil;
  private final ModelMapper modelMapper;

  public Product save(Product product) {
    log.info("Save product {}", product.getSku());
    return productRepository.save(product);
  }

  public long countAllProducts() {
    return productRepository.count();
  }

  public long countProductsByStatus(ProductStatus status) {
    return productRepository.countByStatus(status);
  }

  public Page<ProductListDto> getProductsWithPaging(int page, int size, String sortBy, String dir) {
    Sort.Direction direction =
        dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Product> products = productRepository.findAll(pageable);

    // convert Page<Product> -> Page<ProductListDto>
    return products.map(
        product ->
            new ProductListDto(
                product.getId(),
                product.getSku(),
                product.getPrice(),
                product.getSalePercentage(),
                product.getStatus(),
                product.getBadge(),
                product.getRating()));
  }

  public Page<ProductListDto> searchBySkuContains(
      String sku, int page, int size, String sortBy, String dir) {
    Sort.Direction direction =
        dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Product> products = productRepository.findAllBySkuContainsIgnoreCase(sku, pageable);

    // convert Page<Product> -> Page<ProductListDto>
    return products.map(
        product ->
            new ProductListDto(
                product.getId(),
                product.getSku(),
                product.getPrice(),
                product.getSalePercentage(),
                product.getStatus(),
                product.getBadge(),
                product.getRating()));
  }

  public Product getProductById(long id) {
    return productRepository
        .findById(id)
        .orElseThrow(
            () ->
                new CustomException(
                    messageUtil, "", new String[] {"Product id: " + id}, "error.notExist"));
  }

  @Transactional
  public Product createProduct(ProductRegistration dto) {
    Product product = new Product();
    applyProductRegistration(product, dto);
    return productRepository.save(product);
  }

  @Transactional
  public Product updateProduct(Long productId, ProductRegistration dto) {
    Product product =
        productRepository
            .findByIdFetchAll(productId)
            .orElseThrow(
                () ->
                    new CustomException(
                        messageUtil,
                        "",
                        new String[] {"Product id: " + productId},
                        "error.notExist"));

    applyProductRegistration(product, dto);
    return productRepository.save(product);
  }

  private void applyProductRegistration(Product product, ProductRegistration dto) {
    // basic info
    product.setName(dto.getName());
    product.setPrice(dto.getPrice());

    Integer salePercentage = dto.getSalePercentage();
    product.setSalePercentage(salePercentage);
    product.setSalePrice(getSalePrice(product.getPrice(), salePercentage));

    // top sale 50%
    product.setIsTopSale(salePercentage != null && salePercentage >= 50);

    product.setStockQuantity(dto.getStockQuantity());
    product.setDescription(dto.getDescription());

    // Category
    Set<Category> newCategories = new HashSet<>();
    for (Long categoryId : dto.getCategoryIdList()) {
      newCategories.add(categoryService.getCategoryById(categoryId));
    }
    product.setCategories(newCategories);

    if (product.getId() == null) {
      product.setSku(generateSku("BAG", dto.getCategoryIdList()));
    }

    // Badge
    product.setBadge(ProductBadge.getByClassName(dto.getBadgeClassName()));

    // Status
    product.setStatus(ProductStatus.valueOf(dto.getStatus()));

    // Material and structure
    product.setMaterial(dto.getMaterial());
    product.setInnerPocket(dto.getInnerPocket());
    product.setLockType(dto.getLockType());

    // Strap
    product.setStrapQuantity(dto.getStrapQuantity());
    product.setStrapLength(dto.getStrapLength());
    product.setStrapTotalLength(dto.getStrapTotalLength());
    product.setRemovableStrap(Boolean.TRUE.equals(dto.getRemovableStrap()));
    product.setAdjustableStrap(Boolean.TRUE.equals(dto.getAdjustableStrap()));

    // Size & weight
    product.setWidth(dto.getWidth());
    product.setDepth(dto.getDepth());
    product.setHeight(dto.getHeight());
    product.setHandleLength(dto.getHandleLength());
    product.setWeight(dto.getWeight());

    // Colors
    product.getColors().clear();
    for (ProductColor productColor : dto.getColors()) {
      if (StringUtils.isEmpty(productColor.getName())
          || StringUtils.isEmpty(productColor.getCode())) {
        throw new CustomException(
            messageUtil, "", new String[] {"colorName, colorCode"}, "error.required");
      }
      productColor.setProduct(product);
      product.getColors().add(productColor);
    }

    // Sizes
    product.getSizes().clear();
    for (ProductSize productSize : dto.getSizes()) {
      if (StringUtils.isEmpty(productSize.getName())) {
        throw new CustomException(messageUtil, "", new String[] {"sizeName"}, "error.required");
      }
      productSize.setProduct(product);
      product.getSizes().add(productSize);
    }
  }

  private String generateSku(String prefix, List<Long> categoryIdList) {
    long count = productRepository.count() + 1;
    String categoryIds =
        categoryIdList.stream().map(String::valueOf).collect(Collectors.joining("_"));
    return String.format("%s-%s-%03d", prefix, categoryIds, count);
  }

  private BigDecimal getSalePrice(BigDecimal price, Integer salePercentage) {
    if (price == null || salePercentage == null) {
      return BigDecimal.ZERO;
    }

    // Calculate the percentage of promotion
    BigDecimal percent =
        new BigDecimal(salePercentage).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    // Hold 4 temporary odd numbers to calculate accuracy

    BigDecimal discount = price.multiply(percent);

    // The price after decreasing, rounded to the nearest integer
    return price.subtract(discount).setScale(0, RoundingMode.HALF_UP);
  }

  public ProductRegistration getProductForUpdate(Long productId) {
    Product product = productRepository.findByIdFetchAll(productId).orElse(null);
    ProductRegistration productRegistration = new ProductRegistration();

    if (product != null) {
      productRegistration = modelMapper.map(product, ProductRegistration.class);

      List<Long> categoryIdList = new ArrayList<>();
      product.getCategories().forEach(category -> categoryIdList.add(category.getId()));
      productRegistration.setCategoryIdList(categoryIdList);
    }

    return productRegistration;
  }

  public ProductDto getProductDetailBySku(String sku) {
    Product product = productRepository.findProductBySku(sku).orElse(null);
    ProductDto productDto = new ProductDto();
    if (product != null) {
      Set<ProductImageDto> imageDtoList = productImageRepository.findByProductId(product.getId());
      productDto = modelMapper.map(product, ProductDto.class);
      productDto.setImages(imageDtoList);
    }

    return productDto;
  }

  /** get all product for product list */
  public List<ProductDto> getAllProducts() {
    List<Product> products = productRepository.findAllActiveProducts();
    if (products.isEmpty()) {
      return Collections.emptyList();
    }

    Set<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toSet());
    Map<Long, Set<ProductColorDto>> colorsmap = loadProductColors(productIds);
    Map<Long, Set<ProductImageDto>> imagesMap = loadProductImages(productIds);

    return products.stream()
        .map(product -> mapToProductListDto(product, colorsmap, imagesMap))
        .toList();
  }

  private ProductDto mapToProductListDto(
      Product product,
      Map<Long, Set<ProductColorDto>> colorsmap,
      Map<Long, Set<ProductImageDto>> imagesMap) {
    // model mapper skip null
    product.setColors(null);
    product.setImages(null);

    ProductDto productDto = modelMapper.map(product, ProductDto.class);

    Set<ProductColorDto> productColorDtos =
        colorsmap.getOrDefault(product.getId(), Collections.emptySet());
    Set<ProductImageDto> productImageDtos =
        imagesMap.getOrDefault(product.getId(), Collections.emptySet());

    productDto.setColors(productColorDtos);
    productDto.setImages(productImageDtos);

    return productDto;
  }

  /** Load Images for many Products once (batch loading) */
  private Map<Long, Set<ProductImageDto>> loadProductImages(Set<Long> productIds) {
    if (productIds.isEmpty()) {
      return Collections.emptyMap();
    }

    Set<ProductImage> images = productImageRepository.findByProductIds(productIds);

    return images.stream()
        .sorted(Comparator.comparing(ProductImage::getSortOrder))
        .collect(
            Collectors.groupingBy(
                image -> image.getProduct().getId(),
                Collectors.mapping(
                    this::mapToProductImageDto, Collectors.toCollection(LinkedHashSet::new))));
  }

  /** Load Colors for many Products once (batch loading) */
  private Map<Long, Set<ProductColorDto>> loadProductColors(Set<Long> productIds) {
    if (productIds.isEmpty()) {
      return Collections.emptyMap();
    }

    Set<ProductColor> colors = productColorRepository.findByProductIds(productIds);

    return colors.stream()
        .sorted(Comparator.comparing(ProductColor::getId))
        .collect(
            Collectors.groupingBy(
                color -> color.getProduct().getId(),
                Collectors.mapping(
                    this::mapToProductColorDto, Collectors.toCollection(LinkedHashSet::new))));
  }

  private ProductImageDto mapToProductImageDto(ProductImage image) {
    return new ProductImageDto(
        image.getId(),
        image.getImageUrl(),
        image.getAltText(),
        image.getIsMain(),
        image.getSortOrder(),
        image.getColor().getId(),
        image.getColor().getName(),
        image.getSize().getId(),
        image.getSize().getName());
  }

  private ProductColorDto mapToProductColorDto(ProductColor color) {
    ProductColorDto productColorDto = new ProductColorDto();
    productColorDto.setId(color.getId());
    productColorDto.setName(color.getName());
    productColorDto.setCode(color.getCode());

    List<String> imageUrls =
        color.getImages().stream()
            .sorted(Comparator.comparing(ProductImage::getSortOrder))
            .map(ProductImage::getImageUrl)
            .toList();

    productColorDto.setImageUrls(imageUrls);

    return productColorDto;
  }
}
