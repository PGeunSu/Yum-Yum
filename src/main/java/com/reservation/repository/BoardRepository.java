package com.reservation.repository;

import com.reservation.entity.board.Board;
import com.reservation.entity.user.User;
import com.reservation.type.BoardCategory;
import com.reservation.type.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
  Page<Board> findAllByCategory(BoardCategory category, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndTitleContains(BoardCategory category, String title, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndUserNameContains(BoardCategory category, String nickname, PageRequest pageRequest);
  List<Board> findAllByUser(User loginId);
  List<Board> findAllByCategory(BoardCategory category);
  Long countAllByUserRole(UserType userRole);
  Long countAllByCategory(BoardCategory category);
}