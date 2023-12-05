package vn.eztek.springboot3starter.profile.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.user.entity.Gender;

@Getter
@Setter
public class UpdateProfileRequest {

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String firstName;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String lastName;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 10, message = "length-must-be-less-than-or-equal-20")
  private String phoneNumber;

  private Gender gender;
}
