package vn.eztek.springboot3starter.task.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimplifyTaskResponse implements QueryResult, SocketResponseData {

  private UUID id;
  private String title;
  private UUID assigneeId;
  private UUID stageId;
  private UUID ownerId;
  private UUID nextId;
  private UUID previousId;
  private Boolean hasDescription = false;
}
