package com.reservation.dto;

import com.reservation.entity.user.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private String email;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static UserDto from(User user){
    return UserDto.builder()
        .email(user.getEmail())
        .name(user.getName())
        .createdAt(user.getCreatedAt())
        .modifiedAt(user.getModifiedAt())
        .build();
  }


}
