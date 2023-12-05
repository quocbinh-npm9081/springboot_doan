package vn.eztek.springboot3starter.shared.socket.response;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class CheckListItemDueDateSocketData implements SocketResponseData {

  String taskId;
  String checkListId;
  String checkListItemId;
  ZonedDateTime dueDate;

  public static CheckListItemDueDateSocketData create(String taskId, String checkListId,
      String checkListItemId, ZonedDateTime dueDate) {
    return new CheckListItemDueDateSocketData(taskId, checkListId, checkListItemId, dueDate);
  }

}
