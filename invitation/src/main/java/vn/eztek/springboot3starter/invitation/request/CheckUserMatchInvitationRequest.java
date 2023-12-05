package vn.eztek.springboot3starter.invitation.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CheckUserMatchInvitationRequest {

  @NotBlank(message = "cannot-be-blank")
  String key;
}