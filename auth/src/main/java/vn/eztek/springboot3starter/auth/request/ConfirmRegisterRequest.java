package vn.eztek.springboot3starter.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmRegisterRequest {

  @NotEmpty(message = "cannot-be-empty")
  private String key;

}