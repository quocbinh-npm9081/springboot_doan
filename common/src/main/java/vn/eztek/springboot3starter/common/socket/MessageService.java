package vn.eztek.springboot3starter.common.socket;

import vn.eztek.springboot3starter.shared.socket.NotificationResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;

public interface MessageService {

  void sendEvent(String id, SocketEventType type, SocketForward response);

  void sendEvent(String id, SocketResponseType type, NotificationResponse response);

}
