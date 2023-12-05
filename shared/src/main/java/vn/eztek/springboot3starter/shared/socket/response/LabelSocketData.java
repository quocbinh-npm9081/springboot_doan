package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class LabelSocketData implements SocketResponseData {

  String taskId;
  String labelId;
  String name;
  String color;

  public static LabelSocketData create(String taskId, String labelId, String name, String color) {
    return new LabelSocketData(taskId, labelId, name, color);
  }

}