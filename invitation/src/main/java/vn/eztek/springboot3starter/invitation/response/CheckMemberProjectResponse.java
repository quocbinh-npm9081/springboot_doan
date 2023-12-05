package vn.eztek.springboot3starter.invitation.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
@Value(staticConstructor = "create")
public class CheckMemberProjectResponse implements CommandResult {

  Boolean isMember;
}
