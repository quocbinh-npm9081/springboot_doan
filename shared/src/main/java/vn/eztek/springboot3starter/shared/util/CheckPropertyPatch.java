package vn.eztek.springboot3starter.shared.util;

import com.github.fge.jsonpatch.JsonPatch;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPropertyPatch {

  public static <T> boolean checkFieldsViolation(JsonPatch input, List<String> violateFields) {
    String regex = "path:\\s*\"([^\"]+)\"";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input.toString());
    while (matcher.find()) {
      String path = matcher.group(1).substring(1);
      if (violateFields.contains(path)) {
        return true;
      }
    }
    return false;
  }
}
