package com.planova.server.email.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planova.server.email.request.EmailRequest;
import com.planova.server.global.constants.Constants;
import com.planova.server.global.error.ErrorCode;
import com.planova.server.global.exception.ApiException;
import com.planova.server.user.entity.Provider;
import com.planova.server.user.entity.User;
import com.planova.server.user.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

  private final UserService userService;
  private final Constants constants;
  private final RedisTemplate<String, String> redisTemplate;
  private final JavaMailSender mailSender;
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  /**
   * 이메일 인증
   * 
   * @param EmailRequest (String email, String type)
   * @return String (String message)
   *         - message: "이메일을 성공적으로 보냈습니다. 링크를 통해 진행해주세요."
   */
  public String sendVerificationMail(EmailRequest request) throws MessagingException {
    String email = request.getEmail();
    String type = request.getType();
    String projectName = constants.getProjectName();
    String fromEmail = constants.getEmailSender();
    String clientUrl = constants.getClientUrl();
    String appName = constants.getProjectName();
    String appLogoUrl = constants.getLogo();
    User user = userService.findByEmail(email);

    if ("signup".equals(type)) {
      if (user != null) {
        throw new ApiException(ErrorCode.DUPLICATED_EMAIL);
      }
    } else if ("reset".equals(type)) {
      if (user == null) {
        throw new ApiException(ErrorCode.USER_NOT_FOUND);
      }
      if (user.getProvider() != Provider.이메일) {
        throw new ApiException(ErrorCode.FORBIDDEN_RESET_PASSWORD_SOCIAL_USER);
      }
    } else {
      LOGGER.error("잘못된 이메일 타입: {}", type);
      throw new ApiException(ErrorCode.BAD_REQUEST);
    }

    LOGGER.info("이메일 발송 시작: {}", email);

    String token = UUID.randomUUID().toString();
    String value = projectName + ":" + type + ":" + email;
    redisTemplate.opsForValue().set(token, value, 86400, TimeUnit.SECONDS);

    String url = generateUrl(type, token, clientUrl);

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());

    String subject = generateMailSubject(type, appName);
    String htmlContent = generateMailHtmlContent(type, url, appName, appLogoUrl);

    mailHelper.setFrom(fromEmail);
    mailHelper.setTo(email);
    mailHelper.setSubject(subject);
    mailHelper.setText(htmlContent, true);
    mailSender.send(mimeMessage);

    LOGGER.info("이메일 발송 완료: {}", email);

    return null;
  }

  private String generateUrl(String type, String token, String clientUrl) {
    if ("signup".equals(type)) {
      return clientUrl + "/signup/" + token;
    } else if ("reset".equals(type)) {
      return clientUrl + "/reset-password/" + token;
    }
    return clientUrl;
  }

  private String generateMailSubject(String type, String appName) {
    if ("signup".equals(type)) {
      return appName + " - 회원가입 메일";
    } else if ("reset".equals(type)) {
      return appName + " - 비밀번호 찾기";
    }
    return appName;
  }

  private String generateMailHtmlContent(String type, String url, String appName, String appLogoUrl) {
    String description = "회원가입을 진행해주세요.";
    String actionText = "회원가입이";
    String buttonText = "회원가입";

    if ("reset".equals(type)) {
      description = "비밀번호 찾기를 진행해주세요.";
      actionText = "비밀번호 찾기가";
      buttonText = "비밀번호 찾기";
    }

    return new StringBuilder()
        .append("<div style=\"width: 100%; min-height: 1300px\">")
        .append(
            "<div style=\"text-align: center; width: 800px; margin: 30px auto; padding: 40px 80px; border: 1px solid #ededed; background: #fff; box-sizing: border-box;\">")
        .append("<img style=\"width: 150px\" src=\"").append(appLogoUrl).append("\" alt=\"logo\"/>") // 로고 이미지 URL 사용
        .append("<h1>").append(appName).append("</h1>") // 앱 이름 사용
        .append("<p style=\"padding-top: 20px; font-weight: 700; font-size: 20px; line-height: 1.5; color: #222;\">")
        .append(description)
        .append("</p>")
        .append(
            "<p style=\"font-size: 16px; font-weight: 400; line-height: 1.5; margin-bottom: 40px; color: #6a7282;\">")
        .append("하단 버튼을 누르시면 ").append(actionText).append(" 계속 진행됩니다.")
        .append("</p>")
        .append("<a href=\"").append(url)
        .append(
            "\" target=\"_self\" style=\"background: #404040; text-decoration: none; padding: 10px 24px; font-size: 18px; color: #fff; font-weight: 400; border-radius: 4px;\">")
        .append(buttonText).append("</a>") // URL 사용
        .append("</div>")
        .append("</div>")
        .toString();
  }

  /**
   * 이메일 인증 토큰 확인
   * 
   * @param String (String token)
   * @return String (String email)
   */
  @Override
  public String verifyToken(String token) {
    String value = redisTemplate.opsForValue().get(token);
    if (value != null) {
      String[] parts = value.split(":");
      String email = parts[2];
      return email;
    } else {
      throw new ApiException(ErrorCode.FORBIDDEN);
    }
  }
}