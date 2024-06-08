package com.reservation.service;

import static com.reservation.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.dto.SignInForm;
import com.reservation.entity.User;
import com.reservation.exception.Exception;
import com.reservation.jwt.config.JwtTokenProvider;
import com.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getDescription()));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(passwordEncoder.encode(user.getPassword()))
        .roles(String.valueOf(user.getRole()))
        .build();
  }

  public void userVerify(String email, String code) {
    userService.verifyEmail(email, code);
  }

  public String LoginToken(SignInForm form){
    User user = userService.findValidUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new Exception(LOGIN_CHECK_FAIL));
    return jwtTokenProvider.createToken(user.getEmail(), user.getId());
  }

}
