package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Setter
@Getter
@AllArgsConstructor
public class AssignTaskSocketData implements SocketResponseData {

  String taskId;
  String assigneeId;

  public static AssignTaskSocketData create(String taskId, String assigneeId) {
    return new AssignTaskSocketData(taskId, assigneeId);
  }

}
