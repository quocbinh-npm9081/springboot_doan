package vn.eztek.springboot3starter.publicAccess.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.user.entity.Gender;

@Getter
@Setter
public class RegisteringUserViaLinkRequest {

  @NotBlank(message = "cannot-be-blank")
  private String key;

  @NotBlank(message = "cannot-be-blank")
  @Size(min = 6, max = 50, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-50")
  @Email
  private String username;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String firstName;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String lastName;

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-100")
  private String password;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 20, message = "length-must-be-greater-than-or-equal-20")
  private String phoneNumber;

  private Gender gender;

}
