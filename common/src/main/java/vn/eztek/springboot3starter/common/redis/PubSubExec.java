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

public interface PubSubExec {

  void exec(AccountDeleteMessage event);

  void exec(FileDeleteMessage event);

  void exec(FolderDeleteMessage event);

  void exec(SocketEventMessage event);

  void exec(ProjectDeleteMessage event);

  void exec(AccountRestoreMessage event);

  void exec(ProjectRestoreMessage event);

  void exec(SendMailMessage event);

  void exec(SendEmulatorMessage message);

  void exec(VerifyAccountMessage message);

  void exec(FinishVerifyAccountMessage message);

  void exec(NotificationSocketMessage message);

}
