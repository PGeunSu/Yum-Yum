package com.reservation.repository;

import com.sparta.boardcrud.entity.Board;
import com.sparta.boardcrud.entity.BoardListResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
  List<BoardListResponseDto> findAllByOrderByModifiedAtDesc();
}