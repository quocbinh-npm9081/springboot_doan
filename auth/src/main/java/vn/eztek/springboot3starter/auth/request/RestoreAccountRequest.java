package vn.eztek.springboot3starter.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestoreAccountRequest {

  @NotBlank(message = "cannot-be-blank")
  @Email
  private String email;

}