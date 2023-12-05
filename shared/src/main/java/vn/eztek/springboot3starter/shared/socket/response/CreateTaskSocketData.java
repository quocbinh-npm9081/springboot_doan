package vn.eztek.springboot3starter.shared.socket.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Getter
@Setter
@AllArgsConstructor
public class CreateTaskSocketData implements SocketResponseData {

  TaskDetailSocketData task;
  List<TaskSocketData> taskEffects;

  public static CreateTaskSocketData create(TaskDetailSocketData task,
      List<TaskSocketData> taskEffects) {
    return new CreateTaskSocketData(task, taskEffects);
  }

}
