package com.reservation.service;

import static com.reservation.exception.ErrorCode.POST_NOT_FOUND;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.entity.board.Board;
import com.reservation.entity.board.Like;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
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

//  @Transactional
//  public void addLike(Long boardId, TokenDto loginUserId) {
//    Board board = getPost(boardId);
//    User boardUser = board.getUser();
//    User loginUser = getLoginUser(loginUserId);
//
//
//    // 자신이 누른 좋아요가 아니라면
//    if (!boardUser.equals(loginUser)) {
//      board.likeChange(board.getLikeCnt() - 1);
//    }
//    board.likeChange(board.getLikeCnt() + 1);
//
//    likeRepository.save(Like.builder()
//        .user(loginUser)
//        .board(board)
//        .build());
//  }
//  @Transactional
//  public void deleteLike(String loginId, Long boardId) {
//    Board board = boardRepository.findById(boardId).get();
//    User loginUser = userRepository.findById(Long.valueOf(loginId)).get();
//    User boardUser = board.getUser();
//
//     //자신이 누른 좋아요가 아니라면
//    if (!boardUser.equals(loginUser)) {
//      board.likeChange(board.getLikeCnt() + 1);
//    }
//    board.likeChange(board.getLikeCnt() - 1);
//
//    likeRepository.deleteByUserLoginIdAndBoardId(loginId, boardId);
//  }

  public Boolean checkLike(String loginId, Long boardId) {
    return likeRepository.existsByUserLoginIdAndBoardId(loginId, boardId);
  }

  public Like getLikePost(Long userId) {
    return likeRepository.findById(userId)
        .orElseThrow(() -> new Exception(POST_NOT_FOUND));
  }

  public User getLoginUser(TokenDto userId) {
    return userRepository.findById(userId.getId())
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
  }

  @Transactional
  public void changeLike(Long userId, TokenDto loginId){
    Like boardLike = getLikePost(userId); //게시물에 좋아요한 Id 가져오기
    User loginUser = getLoginUser(loginId); //로그인 유저 Id 가쟈오기
    Board board = boardLike.getBoard(); // 좋아요 누른 게시물 가쟈오기

    //자신이 좋아요를 눌렀던 게시물이 아니라면
    if (!boardLike.getId().equals(loginUser.getId())){
      board.likeChange(board.getLikeCnt() + 1); // 게시글에 좋아요 + 1

      likeRepository.save(Like.builder()
          .user(loginUser)
          .board(board)
          .build());
    }else {
      board.likeChange(board.getLikeCnt() - 1); // 게시글에 좋아요 - 1


      likeRepository.deleteByUserLoginIdAndBoardId(loginUser.getId(), board.getId());
    }
  }
}

