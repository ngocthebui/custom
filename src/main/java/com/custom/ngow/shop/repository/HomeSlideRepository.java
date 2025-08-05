package com.custom.ngow.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.HomeSlide;

@Repository
public interface HomeSlideRepository extends JpaRepository<HomeSlide, Long> {

  List<HomeSlide> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}
