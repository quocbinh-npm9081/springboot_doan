package vn.eztek.springboot3starter.task.request.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLabelRequest {
  @NotBlank(message = "name-cannot-be-null")
  String name;

  @NotBlank(message = "color-cannot-be-null")
  String color;
}
