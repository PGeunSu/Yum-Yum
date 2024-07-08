package com.reservation.entity.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UploadImage {


  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String originalFilename;    // 원본 파일명
  private String savedFilename;        // 서버에 저장된 파일명
}
