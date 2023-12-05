package vn.eztek.springboot3starter.task.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

  @NotNull(message = "title-cannot-be-null")
  String title;
  @NotNull(message = "project-id-cannot-be-null")
  UUID projectId;
  @NotNull(message = "stage-id-cannot-be-null")
  UUID stageId;
  String description;
  UUID previousId;
  UUID nextId;
}
