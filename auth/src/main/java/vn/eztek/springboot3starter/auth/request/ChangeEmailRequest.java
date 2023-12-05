package vn.eztek.springboot3starter.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailRequest {

  @Email
  @Size(min = 6, max = 50, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-50")
  private String username;

  @NotBlank(message = "cannot-be-blank")
  private String key;

}