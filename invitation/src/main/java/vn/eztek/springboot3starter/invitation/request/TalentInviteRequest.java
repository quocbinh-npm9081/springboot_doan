package vn.eztek.springboot3starter.invitation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalentInviteRequest {

  @NotNull(message = "cannot-be-null")
  UUID id;

  @Size(min = 1, message = "required-at-least-one-email")
  List<String> emails;

}
