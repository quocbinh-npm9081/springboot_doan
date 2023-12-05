package vn.eztek.springboot3starter.project.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StageRequest {

  UUID id;

  @NotBlank(message = "name-cannot-be-blank")
  private String name;
}
