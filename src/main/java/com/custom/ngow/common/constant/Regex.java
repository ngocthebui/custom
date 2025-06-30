package com.custom.ngow.common.constant;

public class Regex {

  public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  public static final String USERNAME = "^[a-z][a-z0-9]{3,}$";
  public static final String PASSWORD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";

}
