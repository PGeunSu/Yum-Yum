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
import org.springframework.web.bind.annotation.PathVariable;
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

  //메인화면
  @GetMapping("/main")
  public String mainPage(Model model, Authentication auth) {
    if (auth != null){
      User user = userService.getUser(auth.getName());
      if (user != null){
        model.addAttribute("name", user.getName());
        model.addAttribute("user",user);
      }
    }
    return "index";
  }

  @GetMapping("/signUp")
  public String signUp(Model model) {
    model.addAttribute("signUpForm", new SignUpForm());
    return "user/signUp";
  }

  //회원가입
  @PostMapping("/signUp")
  public String signUp(@Valid SignUpForm form, BindingResult bindingResult) {

    if (userRepository.existsByEmail(form.getEmail())) {
      bindingResult.addError(new FieldError("form", "email", "이메일이 중복됩니다."));
    }
    if (bindingResult.hasErrors()) {
      return "user/signUp";
    }
    userService.userSignUp(form);

    return "redirect:/users/verifyPage";
  }
  //회원 인증 페이지
  @GetMapping("/verifyPage")
  public String verifyPage(Model model){
    return "user/verify";
  }
  //회원가입 인증
  @GetMapping("/verify")
  public String verifyUser(String email, String code, Model model) {
    userService.verifyEmail(email, code);
    model.addAttribute("message", "인증이 완료되었습니다. 로그인 해주세요");
    model.addAttribute("nextUrl", "/users/signIn");
    return "printMessage";
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

    userService.loginToken(form,  response);

    return "redirect:/users/main";
  }

  @GetMapping("/myPage")
  public String userInfo(Model model, Authentication auth) {
    User user = userService.getUser(auth.getName());
    model.addAttribute("user",user);

    return "user/myPage";
  }

  //로그아웃
  @GetMapping("/logout")
  public String logout(HttpServletResponse response) {
    //쿠키 파기
    response.addCookie(userService.logout());
    return "redirect:/users/signIn";
  }

  //회원정보 수정
  @GetMapping("/modify/{userId}")
  public String modify(Model model, @PathVariable Long userId) {
    Optional<User> user = userRepository.findById(userId);
    model.addAttribute("user", user);
    return "user/modify";
  }

  //회원정보 수정
  @PostMapping("/modify")
  public String modify(@Valid UserModifiedDto dto, Model model) {
    userService.modify(dto);
    model.addAttribute("message", "회원 수정이 완료되었습니다.");
    model.addAttribute("nextUrl", "/users/myPage");
    return "printMessage";
  }

  //회원 탈퇴
  @GetMapping("/delete/{userId}")
  public String delete(@PathVariable Long userId, Model model, HttpServletResponse response) {
    userService.delete(userId, response);

    model.addAttribute("message", "탈퇴가 완료되었습니다");
    model.addAttribute("nextUrl", "/users/main");
    return "printMessage";
  }


}
