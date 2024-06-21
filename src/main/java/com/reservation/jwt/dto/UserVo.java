package com.reservation.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserVo {

  private Long id;
  private String name;
  private String email;

}
