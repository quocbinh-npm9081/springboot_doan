package vn.eztek.springboot3starter.task.request.label;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UnAssignLabelInTaskRequest {
  private List<UUID> labelIds;
}
