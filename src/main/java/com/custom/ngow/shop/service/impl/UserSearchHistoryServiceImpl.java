package com.custom.ngow.shop.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.custom.ngow.shop.entity.SearchHistory;
import com.custom.ngow.shop.entity.composite.SearchHistoryId;
import com.custom.ngow.shop.repository.SearchHistoryRepository;
import com.custom.ngow.shop.service.AuthenticationService;
import com.custom.ngow.shop.service.SearchHistoryService;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSearchHistoryServiceImpl implements SearchHistoryService {

  private final SearchHistoryRepository searchHistoryRepository;
  private final AuthenticationService authenticationService;
  private static final int MAX_HISTORY_SIZE = 6;

  @Override
  @Transactional(readOnly = true)
  public List<String> getHistory() {
    String userEmail = authenticationService.getCurrentUserEmail();
    if (userEmail == null) {
      return Collections.emptyList();
    }

    try {
      Pageable pageable = PageRequest.of(0, MAX_HISTORY_SIZE);
      return searchHistoryRepository.findQueriesByUserEmailOrderByLastSearchedDesc(
          userEmail, pageable);
    } catch (Exception e) {
      log.error("Error getting search history for user: {}", userEmail, e);
      return Collections.emptyList();
    }
  }

  @Override
  @Transactional
  public void saveQuery(String query) {
    if (StringUtils.isBlank(query)) {
      return;
    }

    String userEmail = authenticationService.getCurrentUserEmail();
    if (userEmail == null) {
      log.debug("Cannot save search history: user not logged in");
      return;
    }

    query = query.trim();

    try {
      // Tạo composite key
      SearchHistoryId id = new SearchHistoryId(userEmail, query);

      // Tìm existing record
      Optional<SearchHistory> existingHistory = searchHistoryRepository.findById(id);

      if (existingHistory.isPresent()) {
        SearchHistory history = existingHistory.get();
        history.setLastSearched(LocalDateTime.now());
        searchHistoryRepository.save(history);
      } else {
        cleanupOldHistoryIfNeeded(userEmail);

        SearchHistory searchHistory =
            SearchHistory.builder().userEmail(userEmail).searchQuery(query).build();

        searchHistoryRepository.save(searchHistory);

        log.debug("Created new search history for user: {}, query: {}", userEmail, query);
      }

    } catch (Exception e) {
      log.error("Error saving search query '{}' for user: {}", query, userEmail, e);
    }
  }

  @Override
  @Transactional
  public void clearHistory() {
    String userEmail = authenticationService.getCurrentUserEmail();
    if (userEmail == null) {
      return;
    }

    try {
      searchHistoryRepository.deleteAllByUserEmail(userEmail);
      log.info("Cleared search history for user: {}", userEmail);
    } catch (Exception e) {
      log.error("Error clearing search history for user: {}", userEmail, e);
    }
  }

  // Cleanup old records
  private void cleanupOldHistoryIfNeeded(String userEmail) {
    try {
      long currentCount = searchHistoryRepository.countByUserEmail(userEmail);

      if (currentCount >= MAX_HISTORY_SIZE) {
        // Tính số records cần xóa
        int recordsToDelete = (int) (currentCount - MAX_HISTORY_SIZE + 1);

        // Lấy các records cũ nhất
        Pageable pageable = PageRequest.of(0, recordsToDelete);
        List<SearchHistory> oldestRecords =
            searchHistoryRepository.findOldestByUserEmail(userEmail, pageable);

        if (!oldestRecords.isEmpty()) {
          searchHistoryRepository.deleteAll(oldestRecords);
          log.debug(
              "Cleaned up {} old search history records for user: {}",
              oldestRecords.size(),
              userEmail);
        }
      }
    } catch (Exception e) {
      log.error("Error cleaning up old search history for user: {}", userEmail, e);
    }
  }
}
