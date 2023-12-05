package vn.eztek.springboot3starter.common.storage.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.common.property.AzureProperties;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.storage.StorageService;
import vn.eztek.springboot3starter.shared.util.BuildPath;

@Service
@RequiredArgsConstructor
public class AzureStorageServiceImpl implements StorageService {

  private final AzureProperties azureProperties;
  private final BlobContainerClient blobContainerClient;

  @Override
  public String uploadFile(String filePath, MultipartFile file) {
    try {
      BlobClient blob = blobContainerClient.getBlobClient(filePath);
      blob.upload(file.getInputStream(), false);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return String.format("%s/%s/%s", azureProperties.getStorage().getClient().getUri(),
        azureProperties.getStorage().getContainerReference(), filePath);
  }

  @Override
  public void deleteFile(String filePath) {
    blobContainerClient.getBlobClient(filePath).deleteIfExists();
  }

  @Override
  public void deleteFolder(String folderPath) {
    deleteAtLocation(blobContainerClient, folderPath);
  }

  @Override
  public void addKeyEmulatorData(SendEmulatorMessage eventDataSheet) {
    String filePath = BuildPath.buildCsvName();
    BlobClient blob = blobContainerClient.getBlobClient(filePath);
    InputStream inputStream;
    byte[] dataBytes;
    if (blob.exists()) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      blob.downloadStream(outputStream);
      StringBuilder csvData = new StringBuilder();
      for (SendEmulator x : eventDataSheet.getMessages()) {
        csvData.append(x.getEmail()).append(",").append(x.getType()).append(",").append(x.getKey())
            .append(",").append(x.getUrl()).append(System.lineSeparator());
      }
      String newData = outputStream.toString(StandardCharsets.UTF_8) + csvData;
      dataBytes = newData.getBytes(StandardCharsets.UTF_8);
    } else {
      StringBuilder csvData = new StringBuilder().append("email,type,key,url")
          .append(System.lineSeparator());
      for (SendEmulator x : eventDataSheet.getMessages()) {
        csvData.append(x.getEmail()).append(",").append(x.getType()).append(",").append(x.getKey())
            .append(",").append(x.getUrl()).append(System.lineSeparator());
      }
      dataBytes = csvData.toString().getBytes(StandardCharsets.UTF_8);
    }
    inputStream = new ByteArrayInputStream(dataBytes);
    blob.upload(inputStream, dataBytes.length, true);

  }

  private void deleteAtLocation(BlobContainerClient container, String historical) {
    if (checkIfPathExists(container, historical)) {
      List<BlobItem> collect = container.listBlobsByHierarchy(historical).stream().toList();
      for (BlobItem blobItem : collect) {
        String name = blobItem.getName();
        if (name.endsWith("/")) {
          deleteAtLocation(container, name);
        } else {
          container.getBlobClient(name).deleteIfExists();
        }
      }
      container.getBlobClient(historical.substring(0, historical.length() - 1)).deleteIfExists();
    }
  }

  private boolean checkIfPathExists(BlobContainerClient container, String filePath) {
    BlobClient blobClient = container.getBlobClient(filePath);
    return blobClient.exists() || !container.listBlobsByHierarchy(filePath).stream().toList()
        .isEmpty();
  }

}
