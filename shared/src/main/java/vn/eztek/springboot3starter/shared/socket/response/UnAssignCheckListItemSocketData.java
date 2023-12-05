package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Setter
@Getter
@AllArgsConstructor
public class UnAssignCheckListItemSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;

  public static UnAssignCheckListItemSocketData create(String taskId, String checkListId,
      String checkListItemId) {
    return new UnAssignCheckListItemSocketData(taskId, checkListId, checkListItemId);
  }

}
