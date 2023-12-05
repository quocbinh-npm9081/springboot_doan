package vn.eztek.springboot3starter.project.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.UpdateStatusMemberInProjectCommand;
import vn.eztek.springboot3starter.project.command.validated.UpdateStatusMemberInProjectCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;


@Component
@RequiredArgsConstructor
public class UpdateStatusMemberInProjectCommandValidator extends
    CommandValidation<UpdateStatusMemberInProjectCommand, UpdateStatusMemberInProjectCommandValidated> {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final ProjectRepository projectRepository;

  @Override
  public UpdateStatusMemberInProjectCommandValidated validate(
      UpdateStatusMemberInProjectCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    // check PM is active
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (loggedInUser.getStatus().equals(UserStatus.INACTIVE)) {
      throw new BadRequestException("user-is-deactivate");
    }
    // check user deactivate
    var member = userRepository.findByIdAndDeletedAtNull(command.getMemberId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!member.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }

    var project = projectRepository.findByIdAndDeletedAtNull(command.getProjectId())
        .orElseThrow(() -> new NotFoundException("project-not-found"));

    if (!loggedInUser.getRole().getName().equals(RoleName.ADMINISTRATOR) && !project.getOwnerId()
        .equals(loggedInUser.getId())) {
      throw new BadRequestException("you-have-no-right-to-update-this-project");
    }

    if (member.getId().equals(loggedInUser.getId())) {
      throw new BadRequestException("you-cannot-update-status-for-yourself");
    }
    var userProject = userProjectRepository.findByProjectIdAndUserId(project.getId(),
        command.getMemberId()).orElseThrow(() -> new NotFoundException("member-not-found"));

    if (userProject.getStatus().equals(command.getStatus())) {
      throw new BadRequestException("member-already-in-this-status");
    }

    return UpdateStatusMemberInProjectCommandValidated.validatedOf(loggedInUser, member, project,
        userProject);
  }
}
