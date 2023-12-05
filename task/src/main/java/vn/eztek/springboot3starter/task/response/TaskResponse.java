package vn.eztek.springboot3starter.task.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
public class TaskResponse implements CommandResult, QueryResult, SocketResponseData {

  private UUID id;
  private String title;
  private String description;
  private UUID assigneeId;
  private UUID stageId;
  private UUID projectId;
  private UUID ownerId;
  private UUID nextId;
  private UUID previousId;
  private List<AttachmentResponse> attachments;
}
