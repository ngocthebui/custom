package com.custom.ngow.shop.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.dto.ProductRegistration;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;
  private final MessageUtil messageUtil;
  private final ProductColorService productColorService;
  private final ProductSizeService productSizeService;

  public Product save(Product product) {
    log.info("Save product {}", product.getSku());
    return productRepository.save(product);
  }

  public long countAllProducts() {
    return productRepository.count();
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
    Product product = getProductById(productId);

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
    ProductRegistration productRegistration = productRepository.findProductForUpdate(productId);

    Set<Category> categorySet = productRepository.findCategoriesByProductId(productId);
    categorySet.forEach(category -> productRegistration.getCategoryIdList().add(category.getId()));

    productRegistration.setColors(productRepository.findColorsByProductId(productId));
    productRegistration.setSizes(productRepository.findSizesByProductId(productId));

    return productRegistration;
  }
}
