package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class CheckListSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String name;

  public static CheckListSocketData create(String taskId, String checkListId, String name) {
    return new CheckListSocketData(taskId, checkListId, name);
  }

}
