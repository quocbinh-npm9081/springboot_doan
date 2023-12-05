package vn.eztek.springboot3starter.common.storage.azure;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.eztek.springboot3starter.common.property.AzureProperties;

@Configuration
@RequiredArgsConstructor
public class AzureBlobStorageConfiguration {

  private final AzureProperties azureProperties;

  @Bean
  public BlobServiceClient getBlobServiceClient() {
    return new BlobServiceClientBuilder().connectionString(
        azureProperties.getStorage().getConnectionString()).buildClient();

  }

  @Bean
  public BlobContainerClient getBlobContainerClient() {
    return getBlobServiceClient().getBlobContainerClient(
        azureProperties.getStorage().getContainerReference());
  }
}
