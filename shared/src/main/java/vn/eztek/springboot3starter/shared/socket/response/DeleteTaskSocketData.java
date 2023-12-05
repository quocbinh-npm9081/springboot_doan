package vn.eztek.springboot3starter.shared.socket.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Getter
@Setter
@AllArgsConstructor
public class DeleteTaskSocketData implements SocketResponseData {

  String id;
  List<TaskSocketData> taskEffects;

  public static DeleteTaskSocketData create(String id, List<TaskSocketData> taskEffects) {
    return new DeleteTaskSocketData(id, taskEffects);
  }

}
