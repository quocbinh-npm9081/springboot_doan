package vn.eztek.springboot3starter.project.command.validator;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.AddMembersToProjectCommand;
import vn.eztek.springboot3starter.project.command.validated.AddMembersToProjectCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class AddMembersToProjectCommandValidator extends
    CommandValidation<AddMembersToProjectCommand, AddMembersToProjectCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public AddMembersToProjectCommandValidated validate(AddMembersToProjectCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var members = userProjectRepository.findByProjectId(command.getProjectId());

    if (members.isEmpty()) {
      throw new BadRequestException("project-not-found");
    }
    var memberIds = members.stream().map(x -> x.getUser().getId()).toList();
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-are-not-permission-to-invite-talent-to-this-project");
    }

    List<User> users = new ArrayList<>();
    for (String email : command.getEmails()) {
      var existing = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(email).orElseThrow(
          () -> new NotFoundException("user-with-email-[%s]-not-found".formatted(email)));

      var roleName = existing.getRole().getName();
      if (!roleName.equals(RoleName.AGENCY)) {
        throw new BadRequestException("you-can-only-add-agency-member");
      }

      if (!existing.getStatus().equals(UserStatus.ACTIVE)) {
        throw new BadRequestException(
            "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
                email));
      }

      if (memberIds.contains(existing.getId())) {
        throw new BadRequestException(
            "user-with-email-[%s]-has-been-existed-in-this-project".formatted(email));
      }
      users.add(existing);
    }

    return AddMembersToProjectCommandValidated.validatedOf(users, loggedInUser,
        members.get(0).getProject());
  }
}
