package vn.eztek.springboot3starter.project.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.project.command.UpdateProjectCommand;
import vn.eztek.springboot3starter.project.command.validated.UpdateProjectCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class UpdateProjectCommandValidator extends
    CommandValidation<UpdateProjectCommand, UpdateProjectCommandValidated> {

  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public UpdateProjectCommandValidated validate(UpdateProjectCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var project = projectRepository.findByIdAndDeletedAtNull(command.getId())
        .orElseThrow(() -> new NotFoundException("project-not-exists"));

    if (project.getName().equals(command.getName())) {
      throw new BadRequestException("project-name-already-taken");
    }

    var existingName = projectRepository.existsByName((command.getName()));
    if (existingName) {
      throw new BadRequestException("project-name-already-taken");
    }
    if (!project.getOwnerId().equals(existUser.getId())) {
      throw new BadRequestException("you-have-no-right-to-update-this-project");
    }

    return UpdateProjectCommandValidated.validatedOf(project);
  }

}
