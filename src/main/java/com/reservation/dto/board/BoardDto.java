package com.reservation.dto.board;

import com.reservation.entity.board.Board;
import com.reservation.entity.board.UploadImage;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDto {

  private Long id;
  private String userLoginId;   // 유저 아이디
  private String userNickname; // 유저이름
  private String title;
  private String body;
  private Integer likeCnt;
  private Integer commentCnt;
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
  private MultipartFile newImage;
  private UploadImage uploadImage;

  public static BoardDto of(Board board) {
    return BoardDto.builder()
        .id(board.getId())
        .userLoginId(String.valueOf(board.getUser().getId()))
        .userNickname(board.getUser().getName())
        .title(board.getTitle())
        .body(board.getBody())
        .createdAt(board.getCreatedAt())
        .lastModifiedAt(board.getLastModifiedAt())
        .likeCnt(board.getLikes().size())
        .commentCnt(board.getComments().size())
        .uploadImage(board.getUploadImage())
        .build();
  }
}
