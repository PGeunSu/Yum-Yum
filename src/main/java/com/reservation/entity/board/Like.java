package com.reservation.entity.board;

import com.reservation.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "\"like\"")
public class Like {


  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE) // 회원의 계정이 삭제되었을 경우 같이 삭제
  private User user;      // 좋아요를 누른 유저

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE) // 게시글이 삭제되었을 경우 같이 삭제
  private Board board;    // 좋아요가 추가된 게시글
}
