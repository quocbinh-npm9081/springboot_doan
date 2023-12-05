package vn.eztek.springboot3starter.shared.socket.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
public class TaskDetailSocketData implements SocketResponseData {

  private UUID id;
  private String title;
  private String description;
  private UUID assigneeId;
  private UUID stageId;
  private UUID projectId;
  private UUID ownerId;
  private UUID nextId;
  private UUID previousId;
  private List<AttachmentSocketData> attachments;
}
