package com.custom.ngow.shop.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.custom.ngow.shop.entity.composite.SearchHistoryId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SearchHistoryId.class)
public class SearchHistory {

  @Id
  @Column(name = "user_email", nullable = false)
  private String userEmail;

  @Id
  @Column(name = "search_query", nullable = false, length = 500)
  private String searchQuery;

  @UpdateTimestamp private LocalDateTime lastSearched;

  @CreationTimestamp private LocalDateTime createdAt;
}
