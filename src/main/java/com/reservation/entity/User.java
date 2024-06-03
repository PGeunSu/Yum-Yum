package com.reservation.entity;

import static com.reservation.type.Authority.ROLE_USER;

import com.reservation.dto.SignUpForm;
import com.reservation.type.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(value = AuditingEntityListener.class)
public class User implements UserDetails{

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true) //이메일은 unique 함
  private String email;
  private String password;
  private String name;

  @Enumerated(EnumType.STRING)
  private Authority role; //회원 상태

  //인증
  private boolean verify;
  private LocalDateTime verifyExpiredAt;
  private String verificationCode;

  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime modifiedAt;

  public void verificationSuccess(boolean verify){
    this.verify = verify;
  }

  public void setVerificationCode(String verificationCode, LocalDateTime verifyExpiredAt) {
    this.verificationCode = verificationCode;
    this.verifyExpiredAt = verifyExpiredAt;
  }

  public static User from(SignUpForm form){
    return User.builder()
        .email(form.getEmail())
        .name(form.getName())
        .password(form.getPassword())
        .role(ROLE_USER)
        .verify(false)
        .build();
  }



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(this.role.toString()));
    return roles;
  }

  @Override
  public String getUsername() {
    return "";
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
