package vn.eztek.springboot3starter.common.redis.messages;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSocketMessage {

  SocketResponseType notificationType;
  JsonNode metadata;
  SocketResponseType type;
  UUID id;

  public static NotificationSocketMessage create(SocketResponseType notificationType,
      JsonNode metadata, SocketResponseType type, UUID id) {
    return new NotificationSocketMessage(notificationType, metadata, type, id);
  }
}
