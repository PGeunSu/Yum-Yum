package com.reservation.jwt.config;

import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.type.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  private Key key;

  private final long tokenValidTime = 1000L * 60 * 60 * 24;


  public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret){
    byte[] keyBytes = Decoders.BASE64URL.decode(secret);
    this.key =  Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("name", user.getName());
    claims.put("role", user.getRole());

    Date now = new Date();

    return Jwts.builder()
        .signWith(key)
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .setSubject(String.valueOf(user.getId()))
        .compact();
  }

  //Jwt 토큰 정보 추출
  public TokenDto getAuthentication(String token) {
   Claims claims = Jwts.parserBuilder()
       .setSigningKey(key)
       .build()
       .parseClaimsJws(token)
       .getBody();

   return TokenDto.builder()
       .id(Long.valueOf(claims.getSubject()))
       .email(claims.get("email", String.class))
       .name(claims.get("name", String.class))
       .role(UserType.valueOf(claims.get("role", String.class)))
       .build();

  }







}
