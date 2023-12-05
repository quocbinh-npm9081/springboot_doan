package vn.eztek.springboot3starter.common.redis;


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

public interface MessagePublisher {

  void publish(final SocketEventMessage message);

  void publish(final FileDeleteMessage message);

  void publish(final AccountDeleteMessage message);

  void publish(final ProjectDeleteMessage message);

  void publish(final FolderDeleteMessage message);

  void publish(final ProjectRestoreMessage message);

  void publish(final AccountRestoreMessage message);

  void publish(final SendMailMessage message);

  void publish(final SendEmulatorMessage message);

  void publish(VerifyAccountMessage input);

  void publish(FinishVerifyAccountMessage input);

  void publish(NotificationSocketMessage input);

}
