package com.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardListResponsedto {
  // 제목
  private String title;

  // 작성자명
  private String username;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  // Entity -> dto
  public BoardListResponsedto(Board board) {
    this.title = board.getTitle();
    this.createdAt = board.getModifiedAt();
    this.modifiedAt = board.getCreatedAt();
  }

  public BoardListResponsedto(Optional<Board> board) {
    this.title = board.get().getTitle();
    this.createdAt = board.get().getModifiedAt();
    this.modifiedAt = board.get().getCreatedAt();
  }
}
