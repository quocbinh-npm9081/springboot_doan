package vn.eztek.springboot3starter.invitation.command.validated;

import java.util.Map;
import java.util.Set;
import lombok.Value;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class InviteTalentCommandValidated implements CommandValidated {

  Map<String, User> users;
  User loggedInUser;
  Project project;
  Role talentRole;
  Set<Privilege> privilegeTalents;
}
