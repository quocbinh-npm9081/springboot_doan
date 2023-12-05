package vn.eztek.springboot3starter.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-6-and-less-than-100")
  private String password;

  @NotBlank(message = "cannot-be-blank")
  private String key;

}