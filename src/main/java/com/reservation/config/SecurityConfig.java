package com.reservation.config;

import com.reservation.jwt.config.JwtTokenProvider;
import com.reservation.jwt.filter.JwtAuthenticationFilter;
import com.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        .and()
//        .logout() //로그아웃 url
//        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
//        .logoutSuccessUrl("/"); //로그아웃 성공 시 이동할 url
    http
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((auth) ->
            auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/users/**", "/swagger-ui/**",
                    "/v3/**", "/myHandler/**",
                    "/boards/**", "/comments/**", "/messages/**", "/reservation/**",
                    "/restaurant/**", "/likes/**").permitAll() //해당 API 의 요청 허가
                .anyRequest().authenticated()) //이 밖에 모든 요청은 인증 필요
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin((formLogin) -> formLogin
        .loginPage("/users/signIn") //로그인 페이지 url 설정
        .defaultSuccessUrl("/main") //로그인 성공 시 해당 url 반환
        .usernameParameter("email") //로그인 시 사용할 파라미터 이름으로 email 설정
        .failureUrl("/users/login/error")); //로그인 실패 시 이동할 Url
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt Encoder 사용
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


}
