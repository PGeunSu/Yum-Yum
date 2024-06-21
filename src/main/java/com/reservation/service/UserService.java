package com.reservation.service;

import static com.reservation.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.reservation.exception.ErrorCode.ALREADY_VERIFY;
import static com.reservation.exception.ErrorCode.EXPIRE_CODE;
import static com.reservation.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.reservation.exception.ErrorCode.PASSWORD_NOT_MATCH;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;
import static com.reservation.exception.ErrorCode.WRONG_VERIFICATION;

import com.reservation.dto.user.SignInForm;
import com.reservation.dto.user.SignUpForm;
import com.reservation.dto.user.UserDto;
import com.reservation.dto.user.UserModifiedDto;
import com.reservation.entity.user.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.config.JwtTokenProvider;
import com.reservation.jwt.filter.Aes256Util;
import com.reservation.mailgun.SendMailForm;
import com.reservation.repository.UserRepository;
import com.reservation.type.UserType;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }

  public User signUp(SignUpForm form) {
    return userRepository.save(User.from(form));
  }

  private String getVerificationEmail(String email, String name, String code) {
    StringBuilder sb = new StringBuilder();

    return sb.append("Hello ").append(name)
        .append("! Please Click Link for verification. \n")
        .append("http://localhost:8080/users/verify/?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }

  @Transactional
  public UserDto userSignUp(SignUpForm form) {
    if (userRepository.existsByEmail(form.getEmail())) {
      throw new Exception(ALREADY_REGISTER_USER);
    } else {
      User u = signUp(form);
      String code = getRandomCode();
      SendMailForm mailForm = SendMailForm.builder()
          .from("yum-yum@email.com")
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmail(form.getEmail(), form.getName(), code))
          .build();
      validateEmail(u.getId(), code);

      return UserDto.from(u);
    }
  }

  //회원 수정
  @Transactional
  public void modify(UserModifiedDto dto) {
    User user = userRepository.findById(dto.getId())
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));

    user.modify(dto.getName(), dto.getPassword());
  }

  //회원 탈퇴
  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
    if (user.isVerify()) {
      throw new Exception(ALREADY_VERIFY);
    } else if (!user.getVerificationCode().equals(code)) {
      throw new Exception(WRONG_VERIFICATION);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new Exception(EXPIRE_CODE);
    }
    user.verificationSuccess(true);
  }

  public Optional<User> findValidUser(String email, String password) {
    return userRepository.findByEmail(email).filter(
        user -> user.getPassword().equals(password) && user.isVerify());
  }

  public LocalDateTime validateEmail(Long customerId, String verificationCode) {
    Optional<User> optionalCustomer = userRepository.findById(customerId);
    if (optionalCustomer.isPresent()) {
      User u = optionalCustomer.get();
      u.setVerificationCode(verificationCode, LocalDateTime.now().plusDays(1));
      return u.getVerifyExpiredAt();
    }
    return null;
  }

  public String loginToken(SignInForm form) {
    User user = findValidUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new Exception(LOGIN_CHECK_FAIL));
    return jwtTokenProvider.createToken(user.getName(), user.getId(), UserType.USER, form.getEmail());
  }


}
