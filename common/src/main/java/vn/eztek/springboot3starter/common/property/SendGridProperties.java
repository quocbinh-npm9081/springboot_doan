package vn.eztek.springboot3starter.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "sendgrid", ignoreUnknownFields = false)
public class SendGridProperties {

  private String name;

  @NestedConfigurationProperty
  private Client client;

  @NestedConfigurationProperty
  private Path path;

  @NestedConfigurationProperty
  private DynamicTemplateId dynamicTemplateId;
  private String apiKey;
  private String fromMail;

  @Getter
  @Setter
  public static class Client {

    private String uri;

  }

  @Getter
  @Setter
  public static class Path {

    private String registration;
    private String resetPassword;
    private String changeEmail;
    private String confirmAfterChangeEmail;
    private String inviteTalent;
    private String projectDetail;
    private String restoreAccount;
    private String confirmRestoreAccount;
    private String archiveProject;
    private String verifyAccount;
    private String activeAccount;
    private String confirmActiveAccount;

  }

  @Getter
  @Setter
  public static class DynamicTemplateId {

    private String registration;
    private String changePassword;
    private String forgotPassword;
    private String notificationStatus;
    private String changeEmail;
    private String confirmAfterChangeEmail;
    private String inviteTalent;
    private String inviteMember;
    private String activateMemberProject;
    private String deactivateMemberProject;
    private String deleteAccount;
    private String deleteProject;
    private String notificationCompleteDeleteAccount;
    private String notificationCompleteProject;
    private String confirmRestoreAccount;
    private String notificationCancelDeleteProject;
    private String notificationCompleteDeleteProject;
    private String notificationVerifyAccount;
    private String notificationDisableAccount;
    private String activeAccount;
  }

}
