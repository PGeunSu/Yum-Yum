package com.reservation.dto.board;

import com.reservation.entity.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardListResponseDto {
  // 제목
  private String title;

  // 작성자명
  private String username;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  // Entity -> dto
  public BoardListResponseDto(Board board) {

    this.title = board.getTitle();

    this.createdAt = board.getModifiedAt();
    this.modifiedAt = board.getCreatedAt();
  }

  public BoardListResponseDto(Optional<Board> board) {
    if (board.isPresent()){
    this.title = board.get().getTitle();

    this.createdAt = board.get().getModifiedAt();
    this.modifiedAt = board.get().getCreatedAt();
  } else{
      // Optional이 비어있는 경우 처리 (필요에 따라 추가)
      this.title = null;

      this.createdAt = null;
      this.modifiedAt = null;

    }

}
}

