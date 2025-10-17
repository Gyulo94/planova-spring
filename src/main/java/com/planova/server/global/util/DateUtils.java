package com.planova.server.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class DateUtils {

  public static final ZoneId KST = ZoneId.of("Asia/Seoul");

  private DateUtils() {
  }

  public static ZonedDateTime nowKst() {
    return ZonedDateTime.now(KST);
  }

  public static LocalDateTime nowKstLocal() {
    return LocalDateTime.now(KST);
  }

  public static LocalDateTime startOfMonth(ZonedDateTime zdt) {
    ZonedDateTime kst = zdt.withZoneSameInstant(KST);
    LocalDate d = kst.toLocalDate().withDayOfMonth(1);
    return d.atStartOfDay();
  }

  public static LocalDateTime endOfMonth(ZonedDateTime zdt) {
    ZonedDateTime kst = zdt.withZoneSameInstant(KST);
    LocalDate d = kst.toLocalDate();
    LocalDate end = d.withDayOfMonth(d.lengthOfMonth());
    return end.atTime(LocalTime.MAX);
  }

  public static DateRange thisMonthRange() {
    return monthRange(ZonedDateTime.now(KST));
  }

  public static DateRange lastMonthRange() {
    return monthRange(ZonedDateTime.now(KST).minusMonths(1));
  }

  public static DateRange monthRange(ZonedDateTime zdt) {
    return new DateRange(startOfMonth(zdt), endOfMonth(zdt));
  }

  @Getter
  @RequiredArgsConstructor
  public static final class DateRange {
    private final LocalDateTime start;
    private final LocalDateTime end;
  }
}