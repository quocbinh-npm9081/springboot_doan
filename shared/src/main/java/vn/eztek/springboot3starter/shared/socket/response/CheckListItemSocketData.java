package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class CheckListItemSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;
  String content;

  public static CheckListItemSocketData create(String taskId, String checkListId,
      String checkListItemId, String content) {
    return new CheckListItemSocketData(taskId, checkListId, checkListItemId, content);
  }

}
