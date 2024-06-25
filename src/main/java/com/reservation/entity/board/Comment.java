package com.reservation.entity.board;

import com.reservation.enum_class.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.reservation.entity.user.User;

import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment {


  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String body;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;      // 작성자

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;    // 댓글이 달린 게시판

  public void update(String newBody) {
    this.body = newBody;
  }
}
