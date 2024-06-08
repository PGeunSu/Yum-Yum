package com.reservation.service;

import static com.reservation.exception.ErrorCode.ALREADY_VERIFY;
import static com.reservation.exception.ErrorCode.EXPIRE_CODE;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;
import static com.reservation.exception.ErrorCode.WRONG_VERIFICATION;

import com.reservation.dto.SignUpForm;
import com.reservation.dto.UserDto;
import com.reservation.entity.User;
import com.reservation.exception.Exception;
import com.reservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User signUp(SignUpForm form) {
    return userRepository.save(User.from(form));
  }

  @Transactional
  public UserDto userSignUp(SignUpForm form) {
    return UserDto.from(signUp(form));
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

  public Optional<User> findValidUser(String email, String password){
    return userRepository.findByEmail(email).filter(
        user -> user.getPassword().equals(password) && user.isVerify());
  }

  @Transactional
  public LocalDateTime validateEmail(Long customerId, String verificationCode) {
    Optional<User> optionalCustomer = userRepository.findById(customerId);
    if (optionalCustomer.isPresent()) {
      User u = optionalCustomer.get();
      u.setVerificationCode(verificationCode, LocalDateTime.now().plusDays(1));
      return u.getVerifyExpiredAt();
    }
    return null;
  }


}
