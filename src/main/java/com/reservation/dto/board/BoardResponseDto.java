package com.reservation.dto.board;

import com.reservation.entity.board.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardResponseDto {

  private String title;

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  // board의 정보를 받아 boardResponsedto 생성
  public BoardResponseDto(Board board) {
    this.title = board.getTitle();
    this.content = board.getContent();
    this.createdAt = board.getModifiedAt();
    this.modifiedAt = board.getCreatedAt();
  }
}
