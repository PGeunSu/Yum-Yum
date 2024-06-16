package com.reservation.controller;

import com.reservation.dto.user.SignInForm;
import com.reservation.dto.user.SignUpForm;
import com.reservation.dto.user.UserDto;
import com.reservation.dto.user.UserModifiedDto;
import com.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService  userService;

  //회원가입
  @PostMapping("/signUp")
  public ResponseEntity<UserDto> signUp(@RequestBody SignUpForm form){
    return ResponseEntity.ok(userService.userSignUp(form));
  }

  //회원가입 인증
  @GetMapping("/verify")
  public ResponseEntity<String> verifyUser(String email, String code){
    userService.verifyEmail(email, code);
    return ResponseEntity.ok("인증이 완료되었습니다");
  }

  //로그인
  @PostMapping("/signIn")
  public ResponseEntity<String> signIn(@RequestBody SignInForm form){
    return ResponseEntity.ok(userService.loginToken(form));
  }

  //회원정보 수정
  @PutMapping("/modify")
  public ResponseEntity<String> modify(@RequestBody UserModifiedDto dto){
    userService.modify(dto);
    return ResponseEntity.ok("수정이 완료되었습니다");
  }





}
