package com.reservation.service;

import com.reservation.dto.SignUpForm;
import com.reservation.dto.UserDto;
import com.reservation.entity.User;
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

  public User signUp(SignUpForm form){
    return userRepository.save(User.from(form));
  }

  @Transactional
  public UserDto userSignUp(SignUpForm form){
    return UserDto.from(signUp(form));
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
