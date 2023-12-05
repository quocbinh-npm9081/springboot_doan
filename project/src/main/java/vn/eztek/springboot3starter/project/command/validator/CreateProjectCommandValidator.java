package vn.eztek.springboot3starter.project.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.project.command.CreateProjectCommand;
import vn.eztek.springboot3starter.project.command.validated.CreateProjectCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;


@Component
@RequiredArgsConstructor
public class CreateProjectCommandValidator extends
    CommandValidation<CreateProjectCommand, CreateProjectCommandValidated> {

  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public CreateProjectCommandValidated validate(CreateProjectCommand command) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var existing = projectRepository.existsByName((command.getName()));
    if (existing) {
      throw new BadRequestException("project-name-already-taken");
    }
    return CreateProjectCommandValidated.validatedOf(existUser);
  }

}
