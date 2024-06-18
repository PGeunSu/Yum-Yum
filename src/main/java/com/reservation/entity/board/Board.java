package com.reservation.entity.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 생성
@NoArgsConstructor // 기본 생성자를 생성
@Entity // DB 테이블 역할
public class Board extends Timestamped {
  // 글 고유 아이디
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  // 글 제목
  @Column(nullable = false)
  private String title;

  // 글 내용
  @Column(nullable = false)
  private String content;

  // 비밀번호
  @Column(nullable = false)
  private String password;

  // requestDto 정보를 가져와서 entity 만들 때 사용
  public Board(BoardRequestDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.password = requestDto.getPassword();
  }

  // 업데이트 메소드
  public void update(BoardRequestDto requestDto) {
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.password = requestDto.getPassword();
  }

}