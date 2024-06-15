package com.reservation.dto.websocket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto {

  private String message;
  private String writer;

}
