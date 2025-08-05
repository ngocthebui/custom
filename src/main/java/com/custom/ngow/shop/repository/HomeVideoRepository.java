package com.custom.ngow.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.HomeVideo;

@Repository
public interface HomeVideoRepository extends JpaRepository<HomeVideo, Long> {

  HomeVideo findHomeVideoByIsActiveTrue();
}
