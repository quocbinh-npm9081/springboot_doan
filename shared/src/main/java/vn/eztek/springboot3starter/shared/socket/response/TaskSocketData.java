package vn.eztek.springboot3starter.shared.socket.response;

import lombok.Data;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Data
public class TaskSocketData implements SocketResponseData {

  String id;
  String previousId;
  String nextId;
  String stageId;

}
