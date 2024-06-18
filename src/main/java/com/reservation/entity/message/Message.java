package com.reservation.entity.message;

import com.reservation.entity.user.User;
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
import org.springframework.security.core.parameters.P;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String content;

  private boolean deleteBySender;
  private boolean deleteByReceiver;

  @ManyToOne(fetch =  FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  @OnDelete(action = OnDeleteAction.NO_ACTION) // 회원의 계정이 삭제되었을 경우 같이 삭제
  private User sender; //송신자

  @ManyToOne(fetch =  FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  private User receiver; //수신자

  @CreatedDate
  private LocalDateTime createdAt;

  public void deleteBySender(){
    this.deleteBySender = true;
  }

  public void deleteByReceiver(){
    this.deleteByReceiver = true;
  }

  public boolean isDeleted(){
    return isDeleteBySender() && isDeleteByReceiver();
  }



}
