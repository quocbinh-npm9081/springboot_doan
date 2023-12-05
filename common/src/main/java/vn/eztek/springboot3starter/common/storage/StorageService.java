package vn.eztek.springboot3starter.common.storage;

import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;

public interface StorageService {

  String uploadFile(String filePath, MultipartFile file);

  void deleteFile(String filePath);

  void deleteFolder(String folderPath);

  void addKeyEmulatorData(SendEmulatorMessage keyEmulatorData);
}
