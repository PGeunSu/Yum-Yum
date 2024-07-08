package com.reservation.dto.board;

import com.reservation.entity.board.Board;
import com.reservation.entity.board.Comment;
import com.reservation.entity.user.User;
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