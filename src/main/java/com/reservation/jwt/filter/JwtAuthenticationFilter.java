package com.reservation.jwt.filter;

import com.reservation.jwt.config.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    //Jwt 토큰 추출
    String token = resolveToken(request);

    //유효성 검사
    if (token != null && jwtTokenProvider.validateToken(token)){
      //유효할 경우 객체를 가지고와서 SecurityContext 에 저장
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request){
    String bearerToken = request.getHeader(TOKEN_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
      return bearerToken.substring(TOKEN_PREFIX.length());
    }

    return null;

  }
}
