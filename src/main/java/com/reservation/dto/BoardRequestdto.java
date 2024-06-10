package com.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardRequestdto {

  private String title;

  private String content;

  private String password;
}