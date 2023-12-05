package vn.eztek.springboot3starter.task.request.checklist;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCheckListRequest {

  @NotBlank(message = "name-cannot-be-null")
  String name;

}
