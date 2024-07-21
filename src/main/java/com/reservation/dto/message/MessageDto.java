package com.reservation.dto.message;

import com.reservation.entity.message.Message;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  private Long id;
  private String title;
  private String content;
  private String senderName;
  private String receiverName;
  private LocalDateTime time;

  public static MessageDto toDto(Message message){
    return new MessageDto(
        message.getId(),
        message.getTitle(),
        message.getContent(),
        message.getSender().getName(),
        message.getReceiver().getName(),
        message.getCreatedAt()
    );
  }

}
