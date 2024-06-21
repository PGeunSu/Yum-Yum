package com.reservation.jwt.config;

import com.reservation.jwt.dto.JwtDto;
import com.reservation.jwt.dto.UserVo;
import com.reservation.jwt.filter.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
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
public class JwtTokenProvider {

  private final Key key;

  private long tokenValidTime = 1000L * 60 * 60 * 24;

  //application.yml 에서 secret 값 가져오기
  public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public JwtDto generateToken(Authentication authentication) {
    //권한 가져오기
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = (new Date()).getTime();

    //Access Token 생성
    Date accessTokenExpiresln = new Date(now + 86400000);

    String accessToken = Jwts.builder()
        .setSubject(authentication.getName())
        .claim("auth", authorities)
        .setExpiration(accessTokenExpiresln)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    //Refresh Token 생성
    String refreshToken = Jwts.builder()
        .setExpiration(new Date(now + 86400000))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    return JwtDto.builder()
        .grantType("Bearer")
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  //Jwt 토큰 정보 추출
  public Authentication getAuthentication(String accessToken) {
    //Jwt 복호화
    Claims claims = parseClaims(accessToken);

    if (claims.get("auth") == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    //권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities = Arrays.stream(
            claims.get("auth").toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  //토큰에서 사용자 정보 추출
  public UserVo getUserVo(String token) {
    Claims c = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

    return new UserVo(
        Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId()))),
        Aes256Util.decrypt(c.getSubject()),
        Aes256Util.decrypt(c.getAudience()));
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  //토큰 정보 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
    }
    return false;
  }

  public String createToken(String userPk, Long id) {
    Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk))
        .setId(Aes256Util.encrypt(id.toString()));
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .signWith(SignatureAlgorithm.HS256, key)
        .compact();
  }
}
