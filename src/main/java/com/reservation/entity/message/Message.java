package com.reservation.entity.message;

import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(value = AuditingEntityListener.class)
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String content;

  private boolean deletedBySender;
  private boolean deletedByReceiver;

  @ManyToOne(fetch =  FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  @OnDelete(action = OnDeleteAction.CASCADE) // 회원의 계정이 삭제되었을 경우 같이 삭제
  private User sender; //송신자

  @ManyToOne(fetch =  FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User receiver; //수신자

  @CreatedDate
  private LocalDateTime createdAt;

  public boolean isSender(TokenDto user){
    return this.getSender().equals(user);
  }

  public void deleteBySender(){
    this.deletedBySender = true;
  }

  public void deleteByReceiver(){
    this.deletedByReceiver = true;
  }

  public boolean isDeletedMessage(){
    return isDeletedBySender() && isDeletedByReceiver();
  }



}
