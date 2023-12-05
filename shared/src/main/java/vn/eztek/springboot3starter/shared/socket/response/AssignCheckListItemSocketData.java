package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Setter
@Getter
@AllArgsConstructor
public class AssignCheckListItemSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;
  String assigneeId;

  public static AssignCheckListItemSocketData create(String taskId, String checkListId,
      String checkListItemId, String assigneeId) {
    return new AssignCheckListItemSocketData(taskId, checkListId, checkListItemId, assigneeId);
  }

}
