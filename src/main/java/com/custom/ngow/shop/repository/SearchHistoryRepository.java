package com.custom.ngow.shop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.SearchHistory;
import com.custom.ngow.shop.entity.composite.SearchHistoryId;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, SearchHistoryId> {

  @Query(
      "SELECT s.searchQuery FROM SearchHistory s WHERE s.userEmail = :userEmail "
          + "ORDER BY s.lastSearched DESC")
  List<String> findQueriesByUserEmailOrderByLastSearchedDesc(
      @Param("userEmail") String userEmail, Pageable pageable);

  void deleteAllByUserEmail(String userEmail);

  long countByUserEmail(String userEmail);

  @Query(
      "SELECT s FROM SearchHistory s WHERE s.userEmail = :userEmail "
          + "ORDER BY s.lastSearched ASC")
  List<SearchHistory> findOldestByUserEmail(
      @Param("userEmail") String userEmail, Pageable pageable);
}
