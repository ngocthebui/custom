package com.custom.ngow.shop.page.shop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.custom.ngow.shop.service.SearchHistoryCompositeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

  private final SearchHistoryCompositeService searchHistoryCompositeService;

  @GetMapping
  public String search(@RequestParam("q") String query) {
    searchHistoryCompositeService.saveQuery(query);
    return "redirect:/";
  }

  @DeleteMapping("/delete")
  @ResponseBody
  public ResponseEntity<Map<String, String>> deleteHistory() {
    try {
      searchHistoryCompositeService.clearHistory();

      Map<String, String> response = new HashMap<>();
      response.put("status", "success");
      response.put("message", "History deleted successfully");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, String> response = new HashMap<>();
      response.put("status", "error");
      response.put("message", "Delete failed: " + e.getMessage());

      return ResponseEntity.badRequest().body(response);
    }
  }
}
