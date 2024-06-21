package com.reservation.jwt.config;

import com.reservation.jwt.dto.UserVo;
import com.reservation.jwt.filter.Aes256Util;
import com.reservation.service.UserService;
import com.reservation.service.VerifyService;
import com.reservation.type.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final VerifyService verifyService;

  @Value("${spring.jwt.secret}")
  private String key;

  private final long tokenValidTime = 1000L * 60 * 60 * 24;

  public SecretKey getSigningKey(){
    byte[] keyBytes = Decoders.BASE64URL.decode(this.key);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(String name, Long id, UserType userType, String email) {
    Claims claims = Jwts.claims()
        .setSubject(Aes256Util.encrypt(name))
        .setAudience(Aes256Util.encrypt(email))
        .setId(Aes256Util.encrypt(id.toString()));
    claims.put("role",userType);

    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .signWith(this.getSigningKey())
        .compact();
  }

  //토큰 정보 검증
  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(key)
          .build().parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  //토큰에서 사용자 정보 추출
  public UserVo getUserVo(String token) {
    Claims c = Jwts.parserBuilder()
        .setSigningKey(key)
        .build().parseClaimsJws(token).getBody();

    return new UserVo(
        Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId()))),
        Aes256Util.decrypt(c.getSubject()),
        Aes256Util.decrypt(c.getAudience()),
        String.valueOf(c.get("role"))
    );
  }


  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // 토큰을 파싱해서 토큰안 이름 정보 가져오기
  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  //Jwt 토큰 정보 추출
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = verifyService.loadUserByUsername(getUsername(token));

    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities()
    );
  }







}
