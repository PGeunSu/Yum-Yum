package com.reservation.controller;

import com.reservation.dto.user.SignInForm;
import com.reservation.dto.user.SignUpForm;
import com.reservation.dto.user.UserModifiedDto;
import com.reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService  userService;


  @GetMapping("/signUp")
  public String signUp(Model model){
    model.addAttribute("signUpForm", new SignUpForm());
    return "user/signUp";
  }

  //회원가입
  @PostMapping("/signUp")
  public String signUp(@Valid SignUpForm form, BindingResult bindingResult, Model model){

    if(bindingResult.hasErrors()){
      return "user/signUp";
    }
    try{
      userService.userSignUp(form);
    }catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "user/signUp";
    }
    //회원가입 성공시 이메일 인증
    return "user/verify";
  }

  //메인화면
  @GetMapping("/main")
  public String mainPage(){
    return "home";
  }

  //회원가입 인증
  @GetMapping("/verify")
  public String verifyUser(String email, String code, Model model){
    try{
      userService.verifyEmail(email, code);
    }catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "user/verify";
    }

    return "redirect:/";
  }

  //로그인
  @GetMapping("/signIn")
  public String signIn(@Valid SignInForm form){
    userService.loginToken(form);
    return "/user/signIn";
  }

  @GetMapping("/signIn/error")
  public String loginError(Model model){
    model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
    return "/user/signIn";
  }

  //회원정보 수정
  @PutMapping("/modify")
  public ResponseEntity<String> modify(@RequestBody UserModifiedDto dto){
    userService.modify(dto);
    return ResponseEntity.ok("수정이 완료되었습니다");
  }

  //회원 탈퇴
  @DeleteMapping("/delete")
  public ResponseEntity<String> delete(@AuthenticationPrincipal Long id){
    userService.delete(id);
    return ResponseEntity.ok("탈퇴가 완료되었습니다");
  }





}
