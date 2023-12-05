package vn.eztek.springboot3starter.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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

@Service
@AllArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

  private final ObjectMapper objectMapper;
  private final PubSubExec pubSubExec;
  private final Gson gson;

  @Override
  public void onMessage(final Message message, final byte[] pattern) {
    try {
      var messageEvent = objectMapper.readValue(message.getBody(), MessageEvent.class);
      switch (messageEvent.getType()) {
        case DELETE_ACCOUNT -> {
          var value = gson.fromJson(messageEvent.getMessage(), AccountDeleteMessage.class);
          pubSubExec.exec(value);
        }
        case RESTORE_ACCOUNT -> {
          var value = gson.fromJson(messageEvent.getMessage(), AccountRestoreMessage.class);
          pubSubExec.exec(value);
        }
        case DELETE_PROJECT -> {
          var value = gson.fromJson(messageEvent.getMessage(), ProjectDeleteMessage.class);
          pubSubExec.exec(value);
        }
        case RESTORE_PROJECT -> {
          var value = gson.fromJson(messageEvent.getMessage(), ProjectRestoreMessage.class);
          pubSubExec.exec(value);
        }
        case DELETE_FILE -> {
          var value = gson.fromJson(messageEvent.getMessage(), FileDeleteMessage.class);
          pubSubExec.exec(value);
        }
        case DELETE_FOLDER -> {
          var value = gson.fromJson(messageEvent.getMessage(), FolderDeleteMessage.class);
          pubSubExec.exec(value);
        }
        case SOCKET -> {
          var value = gson.fromJson(messageEvent.getMessage(), SocketEventMessage.class);
          pubSubExec.exec(value);
        }
        case SEND_EMAIL -> {
          var value = gson.fromJson(messageEvent.getMessage(), SendMailMessage.class);
          pubSubExec.exec(value);
        }
        case SEND_EMULATOR -> {
          var value = gson.fromJson(messageEvent.getMessage(), SendEmulatorMessage.class);
          pubSubExec.exec(value);
        }
        case VERIFY_ACCOUNT -> {
          var value = gson.fromJson(messageEvent.getMessage(), VerifyAccountMessage.class);
          pubSubExec.exec(value);
        }
        case FINISH_VERIFY_ACCOUNT -> {
          var value = gson.fromJson(messageEvent.getMessage(), FinishVerifyAccountMessage.class);
          pubSubExec.exec(value);
        }
        case NOTIFICATION_SOCKET -> {
          var value = objectMapper.readValue(messageEvent.getMessage(),
              NotificationSocketMessage.class);
          pubSubExec.exec(value);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
