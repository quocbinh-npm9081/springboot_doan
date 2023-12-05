package vn.eztek.springboot3starter.project.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequest {

  @NotBlank(message = "cannot-be-blank")
  private String name;

  private String description;
}
