package vn.eztek.springboot3starter.common.converter;

import java.time.ZonedDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@WritingConverter
public class ZonedDateTimeToStringMongoConverter implements Converter<ZonedDateTime, String> {

  @Override
  public String convert(@Nullable ZonedDateTime zonedDateTime) {
    if (zonedDateTime != null) {
      return zonedDateTime.format(DateUtils.dateTimeFormatter);
    }

    return null;
  }

}
