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
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
  private MultipartFile newImage;
  private UploadImage uploadImage;

  public static BoardDto of(Board board) {
    return BoardDto.builder()
        .id(board.getId())
        .userLoginId(board.getUser().getLoginId())
        .userNickname(board.getUser().getNickname())
        .title(board.getTitle())
        .body(board.getBody())
        .createdAt(board.getCreatedAt())
        .lastModifiedAt(board.getLastModifiedAt())
        .likeCnt(board.getLikes().size())
        .uploadImage(board.getUploadImage())
        .build();
  }
}
