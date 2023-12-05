package vn.eztek.springboot3starter.publicAccess.command.validated;

import java.util.Set;
import lombok.Value;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class InvitationViaLinkCommandValidated implements CommandValidated {

  Invitation invitation;
  Role roleTalent;
  Set<Privilege> privilegeTalents;
}
