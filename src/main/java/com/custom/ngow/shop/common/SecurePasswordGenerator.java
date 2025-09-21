package com.custom.ngow.shop.common;

import java.security.SecureRandom;

public class SecurePasswordGenerator {

  private SecurePasswordGenerator() {}

  private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL = "!@#$%^&*";
  private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

  private static final SecureRandom random = new SecureRandom();

  public static String generateStrongPassword() {
    StringBuilder password = new StringBuilder(8);

    // Make sure there are at least 1 character from each type
    password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
    password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
    password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
    password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

    // Fill remaining 4 characters
    for (int i = 4; i < 8; i++) {
      password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
    }

    // Shuffle password
    return shuffleString(password.toString());
  }

  private static String shuffleString(String string) {
    char[] chars = string.toCharArray();
    for (int i = chars.length - 1; i > 0; i--) {
      int index = random.nextInt(i + 1);
      char temp = chars[index];
      chars[index] = chars[i];
      chars[i] = temp;
    }
    return new String(chars);
  }
}
