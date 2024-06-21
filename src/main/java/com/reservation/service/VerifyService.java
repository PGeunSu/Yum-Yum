package com.reservation.service;

import static com.reservation.exception.ErrorCode.USER_NOT_FOUND;

import com.reservation.exception.Exception;
import com.reservation.jwt.filter.Aes256Util;
import com.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyService {

  private final UserRepository userRepository;

  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    String name = Aes256Util.decrypt(userName);
    return userRepository.findByName(name)
        .orElseThrow(() -> new Exception(USER_NOT_FOUND));
  }

}
