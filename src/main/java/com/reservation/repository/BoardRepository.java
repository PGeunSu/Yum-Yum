package com.reservation.repository;

import com.reservation.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
  List<BoardListResponseDto> findAllByOrderByModifiedAtDesc();
}