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
  Page<Board> findAllByCategoryAndUserRoleNot(BoardCategory category, UserType userRole, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndTitleContainsAndUserRoleNot(BoardCategory category, String title, UserType userRole, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndUserNameContainsAndUserRoleNot(BoardCategory category, String nickname, UserType userRole, PageRequest pageRequest);
  List<Board> findAllByUser(User loginId);
  List<Board> findAllByCategoryAndUserRole(BoardCategory category, UserType userRole);
  Long countAllByUserRole(UserType userRole);
  Long countAllByCategoryAndUserRoleNot(BoardCategory category, UserType userRole);
}