package com.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  //User
  ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디와 패스워드를 확인해 주세요."),

  //Verify
  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
  WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
  EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),

  //reservation
  ALREADY_EXIST_STORE(HttpStatus.BAD_REQUEST,"이미 존재하는 매장 이름입니다."),
  STORE_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 매장입니다.");



  private final HttpStatus httpStatus;
  private final String description;

}
