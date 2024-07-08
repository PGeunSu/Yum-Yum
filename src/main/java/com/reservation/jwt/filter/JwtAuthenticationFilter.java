package com.reservation.jwt.filter;

import com.reservation.entity.user.User;
import com.reservation.jwt.config.JwtTokenProvider;
import com.reservation.jwt.dto.TokenDto;
import com.reservation.service.UserService;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtTokenProvider provider;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    //Jwt 토큰 추출
    String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (request.getCookies() == null){
      chain.doFilter(request,response);
      return;
    }
    Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals("jwtToken"))
        .findFirst()
        .orElse(null);
    if (jwtTokenCookie == null){
      chain.doFilter(request, response);
      return;
    }
    String jwtToken = jwtTokenCookie.getValue();
    tokenHeader = TOKEN_PREFIX + jwtToken;

    if (!tokenHeader.startsWith(TOKEN_PREFIX)){
      chain.doFilter(request, response);
      return;
    }
    String token = tokenHeader.split(" ")[1];
    String loginId =  provider.getAuthentication(token).get("email").toString();

    User user = userService.getUser(loginId);

    // loginUser 정보로 UsernamePasswordAuthenticationToken 발급
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user.getEmail(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    // 권한 부여
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
   chain.doFilter(request, response);

  }

}
