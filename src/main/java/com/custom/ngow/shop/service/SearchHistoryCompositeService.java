package com.custom.ngow.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.service.impl.ClientSearchHistoryServiceImpl;
import com.custom.ngow.shop.service.impl.UserSearchHistoryServiceImpl;

import lombok.RequiredArgsConstructor;

@Service("searchHistoryCompositeService")
@RequiredArgsConstructor
public class SearchHistoryCompositeService implements SearchHistoryService {

  private final AuthenticationService authenticationService;
  private final UserSearchHistoryServiceImpl userSearchHistoryService;
  private final ClientSearchHistoryServiceImpl clientSearchHistoryService;

  @Override
  public List<String> getHistory() {
    return getCurrentService().getHistory();
  }

  @Override
  public void saveQuery(String query) {
    getCurrentService().saveQuery(query);
  }

  @Override
  public void clearHistory() {
    getCurrentService().clearHistory();
  }

  private SearchHistoryService getCurrentService() {
    if (authenticationService.isUserLoggedIn()) {
      return userSearchHistoryService;
    } else {
      return clientSearchHistoryService;
    }
  }
}
