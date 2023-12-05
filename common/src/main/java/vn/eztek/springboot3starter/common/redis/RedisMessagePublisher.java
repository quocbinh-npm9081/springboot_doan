package vn.eztek.springboot3starter.common.redis;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
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
import vn.eztek.springboot3starter.shared.redis.MessageEvent;
import vn.eztek.springboot3starter.shared.redis.MessageEventType;

@Service
@AllArgsConstructor
public class RedisMessagePublisher implements MessagePublisher {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ChannelTopic topic;
  private final Gson gson;

  @Override
  public void publish(SocketEventMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.SOCKET, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(FileDeleteMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.DELETE_FILE, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(AccountDeleteMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.DELETE_ACCOUNT, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(ProjectDeleteMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.DELETE_PROJECT, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(FolderDeleteMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.DELETE_FOLDER, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(ProjectRestoreMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.RESTORE_PROJECT,
        gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(AccountRestoreMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.RESTORE_ACCOUNT,
        gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(SendMailMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.SEND_EMAIL, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(SendEmulatorMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.SEND_EMULATOR, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(VerifyAccountMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.VERIFY_ACCOUNT, gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(FinishVerifyAccountMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.FINISH_VERIFY_ACCOUNT,
        gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }

  @Override
  public void publish(NotificationSocketMessage input) {
    MessageEvent message = MessageEvent.create(MessageEventType.NOTIFICATION_SOCKET,
        gson.toJson(input));
    redisTemplate.convertAndSend(topic.getTopic(), gson.toJson(message));
  }
}
