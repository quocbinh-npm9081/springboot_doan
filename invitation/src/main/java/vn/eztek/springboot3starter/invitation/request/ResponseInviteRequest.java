package vn.eztek.springboot3starter.invitation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseInviteRequest {

  @NotNull(message = "cannot-be-null")
  Boolean accept;
}
