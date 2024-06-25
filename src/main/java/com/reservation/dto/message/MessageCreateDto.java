package com.reservation.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDto {

  @NotBlank(message = "메세지 제목을 입력해주세요")
  private String title;

  @NotBlank(message = "메세지 내용을 입력해주세요")
  private String content;

  @NotBlank(message = "받는 사람 이름을 입력해주세요")
  private String receiverName;



}
