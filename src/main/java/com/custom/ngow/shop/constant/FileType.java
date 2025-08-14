package com.custom.ngow.shop.constant;

import lombok.Getter;

@Getter
public enum FileType {
  IMAGE("image"),
  VIDEO("video");

  private final String displayName;

  FileType(String displayName) {
    this.displayName = displayName;
  }
}
