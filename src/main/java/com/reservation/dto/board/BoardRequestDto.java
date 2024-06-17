package com.reservation.dto.board;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter


@NoArgsConstructor
@Getter
public class BoardRequestDto {

  private String title;

  private String content;

  private String password;
}