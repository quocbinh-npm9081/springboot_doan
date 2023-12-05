package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.redis.MessageEventData;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketEventMessage implements MessageEventData {

  String id;
  SocketEventType type;
  SocketForward response;

  public static SocketEventMessage create(String id, SocketEventType type,
      SocketForward response) {
    return new SocketEventMessage(id, type, response);
  }

}
