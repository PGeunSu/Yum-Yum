package com.reservation.type;

public enum BoardCategory {
  A, B, C;

  public static BoardCategory of(String category) {
    if (category.equalsIgnoreCase("free")) {
      return BoardCategory.A;
    } else if (category.equalsIgnoreCase("greeting")) {
      return BoardCategory.B;
    } else if (category.equalsIgnoreCase("gold")) {
      return BoardCategory.C;
    } else {
      return null;
    }
  }
}
