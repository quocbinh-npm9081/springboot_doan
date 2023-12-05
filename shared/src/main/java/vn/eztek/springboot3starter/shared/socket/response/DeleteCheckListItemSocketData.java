package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class DeleteCheckListItemSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;

  public static DeleteCheckListItemSocketData create(String taskId, String checkListId,
      String checkListItemId) {
    return new DeleteCheckListItemSocketData(taskId, checkListId, checkListItemId);
  }

}
