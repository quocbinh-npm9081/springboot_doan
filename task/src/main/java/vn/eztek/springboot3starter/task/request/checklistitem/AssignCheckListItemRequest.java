package vn.eztek.springboot3starter.task.request.checklistitem;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignCheckListItemRequest {

  @NotNull(message = "userId-cannot-be-null")
  UUID userId;
}
