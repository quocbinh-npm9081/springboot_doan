package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class CheckListItemStatusDateSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;
  Boolean isDone;

  public static CheckListItemStatusDateSocketData create(String taskId, String checkListId,
      String checkListItemId, Boolean isDone) {
    return new CheckListItemStatusDateSocketData(taskId, checkListId, checkListItemId, isDone);
  }

}
