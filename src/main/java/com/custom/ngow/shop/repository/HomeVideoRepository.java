package com.custom.ngow.shop.repository;

import com.custom.ngow.shop.entity.HomeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeVideoRepository extends JpaRepository<HomeVideo, Long> {

    HomeVideo findHomeVideoByIsActiveTrue();
}
