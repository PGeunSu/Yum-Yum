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
  PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  //Message
  MESSAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 쪽지입니다."),

  //Verify
  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
  WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
  EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

  //reservation
  RESERVATION_TIME_WRONG(HttpStatus.BAD_REQUEST,"예약시간은 최소 1시간에서 최대 4시간을 넘길 수 없습니다."),
  STORE_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 매장입니다."),
  DUPLICATED_RESERVATION(HttpStatus.BAD_REQUEST,"이미 예약한 사람이 있습니다."),
  RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "예약 정보를 찾을 수 없습니다."),


  //Board
  POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물입니다.");



  private final HttpStatus httpStatus;
  private final String description;

}
