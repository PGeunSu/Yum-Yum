package com.reservation.service;

import com.reservation.entity.board.Board;
import com.reservation.entity.board.Like;
import com.reservation.entity.user.User;
import com.reservation.repository.BoardRepository;
import com.reservation.repository.LikeRepository;
import com.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final BoardRepository boardRepository;

  @Transactional
  public void addLike(String loginId, Long boardId) {
    Board board = boardRepository.findById(boardId).get();
    User loginUser = userRepository.findByLoginId(loginId).get();
    User boardUser = board.getUser();

    // 자신이 누른 좋아요가 아니라면
    if (!boardUser.equals(loginUser)) {
      boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
    }
    board.likeChange(board.getLikeCnt() + 1);

    likeRepository.save(Like.builder()
        .user(loginUser)
        .board(board)
        .build());
  }

  @Transactional
  public void deleteLike(String loginId, Long boardId) {
    Board board = boardRepository.findById(boardId).get();
    User loginUser = userRepository.findByLoginId(loginId).get();
    User boardUser = board.getUser();

    // 자신이 누른 좋아요가 아니라면
    if (!boardUser.equals(loginUser)) {
      boardUser.likeChange(boardUser.getReceivedLikeCnt() - 1);
    }
    board.likeChange(board.getLikeCnt() - 1);

    likeRepository.deleteByUserLoginIdAndBoardId(loginId, boardId);
  }

  public Boolean checkLike(String loginId, Long boardId) {
    return likeRepository.existsByUserLoginIdAndBoardId(loginId, boardId);
  }
}