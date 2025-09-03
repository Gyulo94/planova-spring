package com.planova.server.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class User {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String name;

  private String email;

  private String password;

  private String image;

  @Enumerated(EnumType.STRING)
  @ColumnDefault("'이메일'")
  private Provider provider;

  @CreatedDate
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }
}