package vn.eztek.springboot3starter.project.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;

@Getter
@Setter
public class UpdateProjectRequest {

  @NotBlank(message = "cannot-be-blank")
  private String name;

  private String description;
  private ProjectStatus status;

}
