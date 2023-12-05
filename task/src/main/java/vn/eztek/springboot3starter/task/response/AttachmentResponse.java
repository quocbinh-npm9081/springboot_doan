package vn.eztek.springboot3starter.task.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class AttachmentResponse implements CommandResult {

  private UUID id;
  private String name;
  private String originalName;
  private UUID taskId;
  private String createdBy;
  private ZonedDateTime createdDate;
  private String lastModifiedBy;
  private ZonedDateTime lastModifiedDate;
}
