package vn.eztek.springboot3starter.shared.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ValidUploadAttachment {

  public static Boolean checkExtension(String fileName) {
    Path path = Paths.get(fileName);
    String fileExtension = getFileExtension(path);
    return switch (fileExtension.toLowerCase()) {
      case "jpg", "jpeg", "png", "pdf" -> true;
      default -> false;
    };
  }

  public static String getFileExtension(Path path) {
    String fileName = path.getFileName().toString();
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex + 1);
    }
    return "";
  }
}
