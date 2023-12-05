package vn.eztek.springboot3starter.shared.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GeneratorUtils {

  private static final String formatDate = "yyyy-MM-dd";
  private static final String formatFileName = "keys-at-%s";

  public static String generateKey() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static List<String> generateUUIDs(int n, List<UUID> excludeUUIDs) {
    Set<String> uuids = new HashSet<>();
    while (uuids.size() < n) {
      String uuid = UUID.randomUUID().toString();
      if (excludeUUIDs == null || excludeUUIDs.isEmpty() || !excludeUUIDs.contains(
          UUID.fromString(uuid))) {
        uuids.add(uuid);
      }
    }
    return uuids.stream().toList();
  }

  public static String genNameCsv() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDate);
    return formatFileName.formatted(currentDate.format(formatter));
  }

}
