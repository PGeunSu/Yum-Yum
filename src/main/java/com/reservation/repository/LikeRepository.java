package com.reservation.repository;

import com.reservation.entity.board.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LikeRepository extends JpaRepository<Like, Long> {
  void deleteByUserIdAndBoardId(Long loginId, Long boardId);
  Boolean existsByUserIdAndBoardId(Long loginId, Long boardId);
  List<Like> findAllByUserId(Long loginId);
}
