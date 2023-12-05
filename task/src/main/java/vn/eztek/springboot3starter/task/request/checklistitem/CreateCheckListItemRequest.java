package vn.eztek.springboot3starter.task.request.checklistitem;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCheckListItemRequest {

  @NotBlank(message = "content-cannot-be-null")
  String content;
}
