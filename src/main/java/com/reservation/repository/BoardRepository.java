package com.reservation.repository;

import com.reservation.entity.board.Board;
import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.type.BoardCategory;
import com.reservation.type.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
  Page<Board> findAllByCategoryAndUserUserRoleNot(BoardCategory category, UserType userRole, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndTitleContainsAndUserUserRoleNot(BoardCategory category, String title, UserType userRole, PageRequest pageRequest);
  Page<Board> findAllByCategoryAndUserNameContainsAndUserUserRoleNot(BoardCategory category, String nickname, UserType userRole, PageRequest pageRequest);
  List<Board> findAllByUser(Long loginId);
  List<Board> findAllByCategoryAndUserUserRole(BoardCategory category, UserType userRole);
  Long countAllByUserUserRole(UserType userRole);
  Long countAllByCategoryAndUserUserRoleNot(BoardCategory category, UserType userRole);
}