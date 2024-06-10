package com.reservation.dto;

mport lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardResponsedto {

  private String title;

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  // board의 정보를 받아 boardResponsedto 생성
  public BoardResponsedto(Board board) {
    this.title = board.getTitle();
    this.content = board.getContent();
    this.createdAt = board.getModifiedAt();
    this.modifiedAt = board.getCreatedAt();
  }
}
