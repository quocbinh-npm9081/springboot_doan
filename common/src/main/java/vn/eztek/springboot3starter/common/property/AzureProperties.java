package vn.eztek.springboot3starter.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "azure", ignoreUnknownFields = false)
public class AzureProperties {

  @NestedConfigurationProperty
  private Storage storage;

  @Getter
  @Setter
  public static class Storage {

    private String containerReference;
    private String connectionString;
    @NestedConfigurationProperty
    private Client client;
  }

  @Getter
  @Setter
  public static class Client {

    private String uri;
  }
}
