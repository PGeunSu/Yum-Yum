package com.reservation.dto.user;

import com.reservation.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModifiedDto {

  private Long id;

  @NotBlank(message = "이메일은 필수 입력 값입니다.")
  private String email;

  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  private String password;


}
