package com.reservation.controller;

import com.reservation.dto.SignInForm;
import com.reservation.dto.SignUpForm;
import com.reservation.dto.UserDto;
import com.reservation.entity.User;
import com.reservation.service.LoginService;
import com.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService  userService;
  private final LoginService loginService;

  //회원가입
  @PostMapping("/signUp")
  public ResponseEntity<UserDto> signUp(@RequestBody SignUpForm form){
    String code = getRandomCode();
    User u = userService.signUp(form);

    userService.validateEmail(u.getId(), code);

    return ResponseEntity.ok(userService.userSignUp(form));
  }

  //회원가입 인증
  @GetMapping("/verify")
  public ResponseEntity<String> verifyUser(String email, String code){
    loginService.userVerify(email, code);
    return ResponseEntity.ok("인증이 완료되었습니다");
  }

  //로그인
  @PostMapping("/signIn")
  public ResponseEntity<String> signIn(@RequestBody SignInForm form){
    return ResponseEntity.ok(loginService.LoginToken(form));
  }

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }



}
