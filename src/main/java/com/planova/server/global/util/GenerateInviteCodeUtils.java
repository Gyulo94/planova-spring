package com.planova.server.global.util;

public class GenerateInviteCodeUtils {
  public static String generateInviteCode(int length) {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int randomIndex = (int) (Math.random() * chars.length());
      code.append(chars.charAt(randomIndex));
    }
    return code.toString();
  }
}
