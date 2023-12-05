package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class AssignLabelInTaskSocketData implements SocketResponseData {

  String taskId;
  String labelId;

  public static AssignLabelInTaskSocketData create(String taskId, String labelId) {
    return new AssignLabelInTaskSocketData(taskId, labelId);
  }
}
