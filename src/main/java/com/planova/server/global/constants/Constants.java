package com.planova.server.global.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class Constants {

  private String clientUrl;
  private String projectName;
  private long emailTokenExpiredAt;
  private String logo;
  private String emailSender;
  private String filePath;
  private String serverUrl;
}