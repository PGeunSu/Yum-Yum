package com.reservation.service;

import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.board.BoardCntDto;
import com.reservation.dto.board.BoardCreateRequest;
import com.reservation.dto.board.BoardDto;
import com.reservation.entity.board.*;
import com.reservation.entity.user.User;
import com.reservation.exception.ErrorCode;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.type.BoardCategory;
import com.reservation.repository.BoardRepository;
import com.reservation.repository.CommentRepository;
import com.reservation.repository.LikeRepository;
import com.reservation.repository.UserRepository;
import com.reservation.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final LikeRepository likeRepository;
  private final CommentRepository commentRepository;
  private final S3UploadService s3UploadService;
  // private final UploadImageService uploadImageService; => 로컬 디렉토리에 저장할 때 사용 => S3UploadService 대신 사용

  public Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword) {
    if (searchType != null && keyword != null) {
      if (searchType.equals("title")) {
        return boardRepository.findAllByCategoryAndTitleContainsAndUserUserRoleNot(category, keyword, UserType.ADMIN, pageRequest);
      } else {
        return boardRepository.findAllByCategoryAndUserNameContainsAndUserUserRoleNot(category, keyword, UserType.ADMIN, pageRequest);
      }
    }
    return boardRepository.findAllByCategoryAndUserUserRoleNot(category, UserType.ADMIN, pageRequest);
  }

  public List<Board> getNotice(BoardCategory category) {
    return boardRepository.findAllByCategoryAndUserUserRole(category, UserType.ADMIN);
  }

  public BoardDto getBoard(Long boardId, String category) {
    Optional<Board> optBoard = boardRepository.findById(boardId);

    // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
    if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
      return null;
    }

    return BoardDto.of(optBoard.get());
  }

  @Transactional
  public Long writeBoard(BoardCreateRequest req, BoardCategory category, TokenDto loginId) throws IOException {
    User loginUser = userRepository.findById(loginId.getId())
        .orElseThrow((() -> new Exception(USER_NOT_FOUND)));

    Board savedBoard = boardRepository.save(req.toEntity(category, loginUser));

    UploadImage uploadImage = s3UploadService.saveImage(req.getUploadImage(), savedBoard);
    if (uploadImage != null) {
      savedBoard.setUploadImage(uploadImage);
    }

    return savedBoard.getId();
  }

  @Transactional
  public Long editBoard(Long boardId, String category, BoardDto dto) throws IOException {
    Optional<Board> optBoard = boardRepository.findById(boardId);

    // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
    if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
      return null;
    }

    Board board = optBoard.get();
    // 게시글에 이미지가 있었으면 삭제
    if (board.getUploadImage() != null) {
      s3UploadService.deleteImage(board.getUploadImage());
      board.setUploadImage(null);
    }

    UploadImage uploadImage = s3UploadService.saveImage(dto.getNewImage(), board);
    if (uploadImage != null) {
      board.setUploadImage(uploadImage);
    }
    board.update(dto);

    return board.getId();
  }

  @Transactional
  public Long deleteBoard(Long boardId, String category) {
    Optional<Board> optBoard = boardRepository.findById(boardId);

    // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
    if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
      return null;
    }

    Board board = optBoard.get();
    User boardUser = board.getUser();
//    boardUser.likeChange(boardUser.getReceivedLikeCnt() - board.getLikeCnt());
    if (board.getUploadImage() != null) {
      s3UploadService.deleteImage(board.getUploadImage());
      board.setUploadImage(null);
    }
    boardRepository.deleteById(boardId);
    return boardId;
  }

  public String getCategory(Long boardId) {
    Board board = boardRepository.findById(boardId).get();
    return board.getCategory().toString().toLowerCase();
  }

  public List<Board> findMyBoard(String category, TokenDto loginId) {
    if (category.equals("board")) {
      return boardRepository.findAllByUser(loginId.getId());
    } else if (category.equals("like")) {
      List<Like> likes = likeRepository.findAllByUserId(loginId.getId());
      List<Board> boards = new ArrayList<>();
      for (Like like : likes) {
        boards.add(like.getBoard());
      }
      return boards;
    } else if (category.equals("comment")) {
      List<Comment> comments = commentRepository.findAllByUserId(loginId.getId());
      List<Board> boards = new ArrayList<>();
      HashSet<Long> commentIds = new HashSet<>();

      for (Comment comment : comments) {
        if (!commentIds.contains(comment.getBoard().getId())) {
          boards.add(comment.getBoard());
          commentIds.add(comment.getBoard().getId());
        }
      }
      return boards;
    }
    return null;
  }

  public BoardCntDto getBoardCnt(){
    return BoardCntDto.builder()
        .totalBoardCnt(boardRepository.count())
        .totalNoticeCnt(boardRepository.countAllByUserUserRole(UserType.ADMIN))
//        .totalGreetingCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.A, UserType.ADMIN))
//        .totalFreeCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.B, UserType.ADMIN))
//        .totalGoldCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.C, UserType.ADMIN))
        .build();
  }
}
