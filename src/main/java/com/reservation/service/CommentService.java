package com.reservation.service;

import com.reservation.dto.board.CommentCreateRequest;
import com.reservation.entity.board.Board;
import com.reservation.entity.board.Comment;
import com.reservation.entity.user.User;
import com.reservation.repository.BoardRepository;
import com.reservation.repository.CommentRepository;
import com.reservation.repository.UserRepository;
import com.reservation.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  public void writeComment(Long boardId, CommentCreateRequest req, Long loginId) {
    Board board = boardRepository.findById(boardId).get();
    User user = userRepository.findById(loginId).get();
    board.commentChange(board.getCommentCnt() + 1);
    commentRepository.save(req.toEntity(board, user));
  }

  public List<Comment> findAll(Long boardId) {
    return commentRepository.findAllByBoardId(boardId);
  }

  @Transactional
  public Long editComment(Long commentId, String newBody, Long loginId) {
    Optional<Comment> optComment = commentRepository.findById(commentId);
    Optional<User> optUser = userRepository.findById(loginId);
    if (optComment.isEmpty() || optUser.isEmpty() || !optComment.get().getUser().equals(optUser.get())) {
      return null;
    }

    Comment comment = optComment.get();
    comment.update(newBody);

    return comment.getBoard().getId();
  }

  public Long deleteComment(Long commentId, Long loginId) {
    Optional<Comment> optComment = commentRepository.findById(commentId);
    Optional<User> optUser = userRepository.findById(loginId);
    if (optComment.isEmpty() || optUser.isEmpty() ||
        (!optComment.get().getUser().equals(optUser.get()) && !optUser.get().getRole().equals(
            UserType.ADMIN))) {
      return null;
    }

    Board board = optComment.get().getBoard();
    board.commentChange(board.getCommentCnt() - 1);

    commentRepository.delete(optComment.get());
    return board.getId();
  }
}

