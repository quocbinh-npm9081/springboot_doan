package vn.eztek.springboot3starter.profile.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-100")
  private String oldPassword;

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-100")
  private String newPassword;
}
