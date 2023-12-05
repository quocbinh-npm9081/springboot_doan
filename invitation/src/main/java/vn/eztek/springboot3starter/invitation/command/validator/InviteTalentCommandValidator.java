package vn.eztek.springboot3starter.invitation.command.validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.role.repository.RoleRepository;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.validated.InviteTalentCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;


@Component
@RequiredArgsConstructor
public class InviteTalentCommandValidator extends
    CommandValidation<InviteTalentCommand, InviteTalentCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final RoleRepository roleRepository;

  @Override
  public InviteTalentCommandValidated validate(InviteTalentCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    if (command.getEmails() == null || command.getEmails().isEmpty()) {
      throw new BadRequestException("email-is-required");
    }

    var roleTalent = roleRepository.findByName(RoleName.TALENT)
        .orElseThrow(() -> new NotFoundException("role-not-found"));

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    Set<Privilege> privilegeTalents = new HashSet<>();
    for (Privilege privilege : roleTalent.getPrivileges()) {
      privilegeTalents.add(new Privilege(privilege.getId(), privilege.getName()));
    }

    var userProjects = userProjectRepository.findByProjectId(command.getProjectId());
    if (userProjects.isEmpty()) {
      throw new NotFoundException("project-not-found");
    }

    if (loggedInUser.getStatus().equals(UserStatus.INACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var memberJoinIds = userProjects.stream()
        .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED)).map(x -> x.getUser().getId())
        .toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-invite-talent-in-this-project");
    }

    Map<String, User> users = new HashMap<>();
    for (String email : command.getEmails()) {
      if (email.isBlank()) {
        throw new BadRequestException("invalid-email");
      }
      var existing = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(email).orElse(null);
      // if account not exist create account with email
      if (existing == null) {
        users.put(email, null);
      } else {
        // check account deactivate
        if (existing.getStatus().equals(UserStatus.INACTIVE)) {
          throw new BadRequestException(
              "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
                  email));
        }
        var memberIds = userProjects.stream().map(x -> x.getUser().getId()).toList();
        if (memberIds.contains(existing.getId())) {
          throw new BadRequestException(
              "user-with-email-[%s]-has-been-existed-in-this-project".formatted(email));
        }

        if (!existing.getRole().getName().equals(RoleName.TALENT)) {
          throw new BadRequestException("user-with-email-[%s]-is-not-talent".formatted(email));
        }
        users.put(email, existing);
      }
    }
    return InviteTalentCommandValidated.validatedOf(users, loggedInUser,
        userProjects.get(0).getProject(), roleTalent, privilegeTalents);
  }
}
