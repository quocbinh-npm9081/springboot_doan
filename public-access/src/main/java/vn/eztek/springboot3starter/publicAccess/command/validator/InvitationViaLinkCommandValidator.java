package vn.eztek.springboot3starter.publicAccess.command.validator;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.role.repository.RoleRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.publicAccess.command.InvitationViaLinkCommand;
import vn.eztek.springboot3starter.publicAccess.command.validated.InvitationViaLinkCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.ConflictException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvitationViaLinkCommandValidator
    extends CommandValidation<InvitationViaLinkCommand, InvitationViaLinkCommandValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;
  private final RoleRepository roleRepository;
  private final KeyRepository keyRepository;

  @Override
  public InvitationViaLinkCommandValidated validate(InvitationViaLinkCommand command) {
    var invitation = invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(
            command.getKey(),
            DateUtils.currentZonedDateTime(),
            InvitationType.INVITE_TALENT_BY_LINK)
        .orElseThrow(() -> new BadRequestException("invalid-invitation"));

    if (userRepository.existsByUsernameIgnoreCase(command.getUsername())) {
      throw new ConflictException("username-already-exists");
    }

    var roleTalent = roleRepository.findByName(RoleName.TALENT)
        .orElseThrow(() -> new NotFoundException("role-not-found"));

    Set<Privilege> privilegeTalents = new HashSet<>();
    for (Privilege privilege : roleTalent.getPrivileges()) {
      privilegeTalents.add(new Privilege(privilege.getId(), privilege.getName()));
    }

    return InvitationViaLinkCommandValidated.validatedOf(invitation, roleTalent,
        privilegeTalents);
  }

}
