package vn.eztek.springboot3starter.project.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStagesRequest {

  @Size(min = 1, message = "required-at-least-one-stage")
  private List<@Valid StageRequest> stages;
}

