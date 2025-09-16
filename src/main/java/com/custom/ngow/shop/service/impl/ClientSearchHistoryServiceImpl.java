package com.custom.ngow.shop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.service.SearchHistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientSearchHistoryServiceImpl implements SearchHistoryService {

  public static final String SEARCH_HISTORY_KEY = "searchHistory";
  private static final int MAX_HISTORY_SIZE = 6;

  private final HttpServletRequest request;

  @Override
  public List<String> getHistory() {
    HttpSession session = request.getSession(true);

    @SuppressWarnings("unchecked")
    List<String> history = (List<String>) session.getAttribute(SEARCH_HISTORY_KEY);

    if (history == null) {
      return Collections.emptyList();
    }

    return new ArrayList<>(history);
  }

  @Override
  public void saveQuery(String query) {
    if (query == null || query.trim().isEmpty()) {
      return;
    }

    query = query.trim();
    HttpSession session = request.getSession(true); // Create session if not exists

    @SuppressWarnings("unchecked")
    List<String> history = (List<String>) session.getAttribute(SEARCH_HISTORY_KEY);

    if (history == null) {
      history = new ArrayList<>();
    } else {
      history = new ArrayList<>(history);
    }

    // Remove existing query to avoid duplicates
    history.remove(query);

    // Add to beginning
    history.addFirst(query);

    // Limit size
    if (history.size() > MAX_HISTORY_SIZE) {
      history = new ArrayList<>(history.subList(0, MAX_HISTORY_SIZE));
    }

    session.setAttribute(SEARCH_HISTORY_KEY, history);
  }

  @Override
  public void clearHistory() {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(SEARCH_HISTORY_KEY);
    }
  }
}
