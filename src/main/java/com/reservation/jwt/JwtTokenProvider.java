package com.reservation.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider{

  private final Key key;

  private long tokenValidTime = 1000L * 60 * 60 * 24;

  //application.yml 에서 secret 값 가져오기
  public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey){
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

}
