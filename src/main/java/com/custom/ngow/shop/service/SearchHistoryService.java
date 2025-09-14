package com.custom.ngow.shop.service;

import java.util.List;

public interface SearchHistoryService {

  List<String> getHistory();

  void saveQuery(String query);

  void clearHistory();
}
