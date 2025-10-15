package com.planova.server.global.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer customizer() {
    return builder -> builder.deserializerByType(LocalDateTime.class, new KSTLocalDateTimeDeserializer());
  }

  public static class KSTLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      String value = p.getText();
      try {
        ZonedDateTime zdt = ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        return zdt.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
      } catch (Exception e) {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      }
    }
  }
}