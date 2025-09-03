package com.planova.server.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planova.server.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * 이메일로 회원 정보 조회
   * 
   * @param String (String email)
   * @return UserResponse (UUID id, String name, String email, String provider,
   *         LocalDateTime createdAt)
   */
  User findByEmail(String email);
}