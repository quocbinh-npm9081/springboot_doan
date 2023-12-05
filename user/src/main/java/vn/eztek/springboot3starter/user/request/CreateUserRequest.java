package vn.eztek.springboot3starter.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.Gender;

@Getter
@Setter
public class CreateUserRequest {

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String firstName;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 50, message = "length-must-be-less-than-or-equal-50")
  private String lastName;

  @NotBlank(message = "cannot-be-blank")
  @Size(min = 6, max = 50, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-50")
  @Email
  private String username;

  @NotEmpty(message = "cannot-be-empty")
  @Size(min = 6, max = 100, message = "length-must-be-greater-than-or-equal-6-and-less-than-or-equal-100")
  private String password;

  @NotBlank(message = "cannot-be-blank")
  @Size(max = 20, message = "length-must-be-greater-than-or-equal-20")
  private String phoneNumber;


  private Gender gender;

  @NotNull(message = "cannot-be-null")
  private RoleName role;

  private Set<PrivilegeName> privileges;

}
