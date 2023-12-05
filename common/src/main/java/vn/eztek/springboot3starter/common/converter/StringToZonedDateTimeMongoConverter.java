package vn.eztek.springboot3starter.common.converter;

import java.time.ZonedDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@ReadingConverter
public class StringToZonedDateTimeMongoConverter implements Converter<String, ZonedDateTime> {

  @Override
  public ZonedDateTime convert(@Nullable String string) {
    if (string != null) {
      return ZonedDateTime.parse(string, DateUtils.dateTimeFormatter);
    }

    return null;
  }

}
