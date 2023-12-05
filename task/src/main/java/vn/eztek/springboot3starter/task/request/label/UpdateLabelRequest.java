package vn.eztek.springboot3starter.task.request.label;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLabelRequest {
  String name;
  String color;
}
