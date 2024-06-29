package com.reservation.controller;

import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.user.SignInForm;
import com.reservation.dto.user.SignUpForm;
import com.reservation.dto.user.UserModifiedDto;
import com.reservation.entity.user.User;
import com.reservation.exception.ErrorCode;
import com.reservation.exception.Exception;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.repository.UserRepository;
import com.reservation.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner.Mode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserRepository userRepository;


  @GetMapping("/signUp")
  public String signUp(Model model) {
    model.addAttribute("signUpForm", new SignUpForm());
    return "user/signUp";
  }

  //회원가입
  @PostMapping("/signUp")
  public String signUp(@Valid SignUpForm form, BindingResult bindingResult,
      Model model) {

    if (userRepository.existsByEmail(form.getEmail())) {
      bindingResult.addError(new FieldError("form", "email", "이메일이 중복됩니다."));
    }
    if (bindingResult.hasErrors()) {
      return "user/signUp";
    }
    try {
      userService.userSignUp(form);
    }catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
    }

    return "redirect:/users/signIn";
  }

  //메인화면
  @GetMapping("/main")
  public String mainPage(Model model, Authentication auth) {
    if (auth != null){
      User user = userService.getUser(auth.getName());
      if (user != null){
        model.addAttribute("name", user.getName());
      }
    }

    return "home";
  }

  //회원가입 인증
  @GetMapping("/verify")
  public String verifyUser(String email, String code, Model model) {
    try {
      userService.verifyEmail(email, code);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "user/verify";
    }

    return "redirect:/";
  }

  //로그인뷰
  @GetMapping("/signIn")
  public String signIn(Model model) {
    model.addAttribute("signInForm", new SignInForm());
    return "user/signIn";
  }

  //로그인
  @PostMapping("/signIn")
  public String login(@Valid @ModelAttribute SignInForm form, BindingResult bindingResult,
      HttpServletResponse response) {

    User user = userService.login(form);

    if (user == null) {
      bindingResult.reject("loginFail", "이메일 또는 비밀번호가 틀렸습니다");
    }

    if (bindingResult.hasErrors()) {
      return "user/signIn";
    }

    String token = userService.loginToken(form);

    Cookie cookie = new Cookie("jwtToken", token);
    cookie.setMaxAge(60 * 60); //시간
    response.addCookie(cookie);

    return "redirect:/users/main";
  }

  @GetMapping("/info")
  public String userInfo(Model model, Authentication auth) {
    User user = userService.getUser(auth.getName());
    model.addAttribute("user",user);

    return "info";
  }


  //로그아웃
  @GetMapping("/logout")
  public String logout(HttpServletResponse response) {
    //쿠키 파기
    Cookie cookie = new Cookie("jwtToken", null);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return "redirect:/users/signIn";
  }

  @GetMapping("/signIn/error")
  public String loginError(Model model) {
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
    return "/user/signIn";
  }

  //회원정보 수정
  @PutMapping("/modify")
  public ResponseEntity<String> modify(@RequestBody UserModifiedDto dto) {
    userService.modify(dto);
    return ResponseEntity.ok("수정이 완료되었습니다");
  }

  //회원 탈퇴
  @DeleteMapping("/delete")
  public ResponseEntity<String> delete(@AuthenticationPrincipal Long id) {
    userService.delete(id);
    return ResponseEntity.ok("탈퇴가 완료되었습니다");
  }


}
