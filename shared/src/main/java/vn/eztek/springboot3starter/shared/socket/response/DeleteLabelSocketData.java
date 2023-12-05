package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class DeleteLabelSocketData implements SocketResponseData {

  String taskId;
  String labelId;

  public static DeleteLabelSocketData create(String taskId, String labelId) {
    return new DeleteLabelSocketData(taskId, labelId);
  }

}