package vn.eztek.springboot3starter.invitation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
@AllArgsConstructor
public class CheckUserMatchInviteResponse implements CommandResult {

  Boolean isMatch;
}
