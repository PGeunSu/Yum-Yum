package com.reservation.jwt.dto;

import com.reservation.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

  private Long id;
  private String email;
  private String name;
  private UserType role;

}
