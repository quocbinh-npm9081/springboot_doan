package vn.eztek.springboot3starter.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveAccountRequest {

  @NotBlank(message = "cannot-be-blank")
  private String email;

}