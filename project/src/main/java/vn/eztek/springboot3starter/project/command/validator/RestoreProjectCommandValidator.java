package vn.eztek.springboot3starter.project.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.project.command.RestoreProjectCommand;
import vn.eztek.springboot3starter.project.command.validated.RestoreProjectCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class RestoreProjectCommandValidator extends
    CommandValidation<RestoreProjectCommand, RestoreProjectCommandValidated> {

  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public RestoreProjectCommandValidated validate(RestoreProjectCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var project = projectRepository.findById(command.getId())
        .orElseThrow(() -> new NotFoundException("project-not-exists"));

    if (!project.getOwnerId().equals(existUser.getId())) {
      throw new BadRequestException("you-have-no-right-to-update-this-project");
    }

    if (project.getDeletedAt() == null) {
      throw new BadRequestException("project-is-not-deleted");
    }
    return RestoreProjectCommandValidated.validatedOf(project, existUser);
  }

}
