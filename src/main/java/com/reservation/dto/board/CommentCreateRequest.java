package com.reservation.dto.board;

import com.reservation.entity.board.Board;
import com.reservation.entity.Comment;
 import com.reservation.entity.User;  // 유저연동
import lombok.Data;

@Data
public class CommentCreateRequest {


  private String body;

  public Comment toEntity(Board board, User user) {
    return Comment.builder()
        .user(user)
        .board(board)
        .body(body)
        .build();
  }
}
d