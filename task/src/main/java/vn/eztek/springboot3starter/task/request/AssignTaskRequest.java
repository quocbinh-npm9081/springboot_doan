package vn.eztek.springboot3starter.task.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTaskRequest {

  @NotNull(message = "id-cannot-be-null")
  UUID userId;
}
