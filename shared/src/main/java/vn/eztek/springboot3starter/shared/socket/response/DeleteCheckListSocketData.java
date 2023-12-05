package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class DeleteCheckListSocketData implements SocketResponseData {

  String taskId;
  String checkListId;

  public static DeleteCheckListSocketData create(String taskId, String checkListId) {
    return new DeleteCheckListSocketData(taskId, checkListId);
  }

}
