package vn.eztek.springboot3starter.task.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchUpdateTaskRequest {

  @NotBlank(message = "can-not-be-blank")
  UUID id;

  @NotBlank(message = "can-not-be-blank")
  UUID stageId;

  UUID preTaskId;
  UUID nextTaskId;
}
