package com.reservation.repository;

import com.reservation.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByBoardId(Long boardId);
  List<Comment> findAllByUserId(Long loginId);
}
