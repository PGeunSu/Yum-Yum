package com.reservation.repository;

import com.reservation.entity.user.User;
import com.reservation.jwt.dto.TokenDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByName(String name);

  boolean findByVerifyTrue();
}
