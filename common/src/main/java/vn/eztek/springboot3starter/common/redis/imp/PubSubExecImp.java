package vn.eztek.springboot3starter.common.redis.imp;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.email.EmailService;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.quartz.JobService;
import vn.eztek.springboot3starter.common.redis.PubSubExec;
import vn.eztek.springboot3starter.common.redis.mapper.MessageMapper;
import vn.eztek.springboot3starter.common.redis.messages.AccountDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.AccountRestoreMessage;
import vn.eztek.springboot3starter.common.redis.messages.FileDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.FinishVerifyAccountMessage;
import vn.eztek.springboot3starter.common.redis.messages.FolderDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.NotificationSocketMessage;
import vn.eztek.springboot3starter.common.redis.messages.ProjectDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.ProjectRestoreMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.common.redis.messages.VerifyAccountMessage;
import vn.eztek.springboot3starter.common.socket.MessageService;
import vn.eztek.springboot3starter.common.storage.StorageService;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.shared.util.BuildKeyQuartz;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Service
@RequiredArgsConstructor
public class PubSubExecImp implements PubSubExec {

  private final JobService jobService;
  private final StorageService storageService;
  private final MessageService messageService;
  private final EmailService emailService;
  private final SendGridProperties sendGridProperties;
  private final NotificationRepository notificationRepository;
  private final MessageMapper messageMapper;

  @Override
  public void exec(AccountDeleteMessage messageEvent) {
    try {

      // send email
      var link = sendGridProperties.getClient().getUri() + sendGridProperties.getPath()
          .getRestoreAccount();

      var templateData = new HashMap<String, String>();
      templateData.put("name", messageEvent.getName());
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime().plusDays(30)));
      templateData.put("link", link);
      emailService.sendHtmlMessage(messageEvent.getEmail(),
          sendGridProperties.getDynamicTemplateId().getDeleteAccount(), templateData);

      jobService.schedulePermanentDeleteUser(messageEvent);

    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(ProjectDeleteMessage messageEvent) {
    try {
      // send email
      var link =
          sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getArchiveProject()
              .formatted(messageEvent.getId());

      var templateData = new HashMap<String, String>();
      templateData.put("name", messageEvent.getName());
      templateData.put("projectName", messageEvent.getName());
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime().plusDays(30)));
      templateData.put("link", link);
      emailService.sendHtmlMessage(messageEvent.getEmail(),
          sendGridProperties.getDynamicTemplateId().getDeleteProject(), templateData);

      jobService.schedulePermanentDeleteProject(messageEvent);
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(FileDeleteMessage messageEvent) {
    storageService.deleteFile(messageEvent.getFilePath());
  }

  @Override
  public void exec(FolderDeleteMessage messageEvent) {
    storageService.deleteFolder(messageEvent.getFolderPath());
  }

  @Override
  public void exec(SocketEventMessage event) {
    messageService.sendEvent(event.getId(), event.getType(), event.getResponse());
  }

  @Override
  public void exec(AccountRestoreMessage event) {
    var id = event.getId().toString();
    try {

      var jobKey = JobKey.jobKey(BuildKeyQuartz.buildJobKey(BuildKeyQuartz.USER, id));
      var triggerKey = TriggerKey.triggerKey(
          BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.USER, id));
      jobService.cancelSchedule(jobKey, triggerKey);

      //TODO Send notification for user project

    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(ProjectRestoreMessage messageEvent) {
    var id = messageEvent.getId().toString();
    try {

      var jobKey = JobKey.jobKey(BuildKeyQuartz.buildJobKey(BuildKeyQuartz.PROJECT, id));
      var triggerKey = TriggerKey.triggerKey(
          BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.PROJECT, id));
      jobService.cancelSchedule(jobKey, triggerKey);

      // send mail notification
      var link =
          sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getProjectDetail()
              .formatted(messageEvent.getId());
      var templateData = new HashMap<String, String>();
      templateData.put("projectName", messageEvent.getName());
      templateData.put("name", messageEvent.getUsername());
      templateData.put("link", link);
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime().plusDays(30)));
      emailService.sendHtmlMessage(messageEvent.getEmail(),
          sendGridProperties.getDynamicTemplateId().getNotificationCancelDeleteProject(),
          templateData);

      //TODO Send notification for user project

    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(SendMailMessage message) {
    emailService.sendHtmlMessage(message.getEmail(), message.getTemplateId(),
        message.getTemplateData());
  }

  @Override
  public void exec(SendEmulatorMessage message) {
    storageService.addKeyEmulatorData(message);
  }

  @Override
  public void exec(VerifyAccountMessage message) {
    try {
      // send email
      var link =
          sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getVerifyAccount()
              + message.getKeyRandom();
      var templateData = new HashMap<String, String>();
      templateData.put("name", message.getFirstName() + " " + message.getLastName());
      templateData.put("link", link);
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
      templateData.put("email", message.getUsername());
      emailService.sendHtmlMessage(message.getUsername(),
          sendGridProperties.getDynamicTemplateId().getNotificationVerifyAccount(), templateData);
      jobService.scheduleUpdateStatusAccount(message);

    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(FinishVerifyAccountMessage message) {
    try {
      var id = message.getUserId();
      var jobKey = JobKey.jobKey(BuildKeyQuartz.buildJobKey(BuildKeyQuartz.VERIFY_ACCOUNT, id));
      var triggerKey = TriggerKey.triggerKey(
          BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.VERIFY_ACCOUNT, id));
      jobService.cancelSchedule(jobKey, triggerKey);
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void exec(NotificationSocketMessage message) {
    var notification = messageMapper.mapBeforeCreateNotification(message.getNotificationType(),
        message.getMetadata(), message.getId());
    notification = notificationRepository.save(notification);
    var notificationResponse = messageMapper.mapToNotificationResponse(notification);
    messageService.sendEvent(message.getId().toString(), message.getType(), notificationResponse);
  }
}
