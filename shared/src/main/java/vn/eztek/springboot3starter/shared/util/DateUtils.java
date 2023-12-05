package vn.eztek.springboot3starter.shared.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public final static DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

  public static ZonedDateTime currentZonedDateTime() {
    return ZonedDateTime.now();
  }

  public static ZonedDateTime longToZonedDateTime(Long date) {
    var instant = Instant.ofEpochMilli(date);
    return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
  }

  public static String zonedDateTimeToString(ZonedDateTime dateTime) {
    return dateTime.format(dateTimeFormatter);
  }

}
