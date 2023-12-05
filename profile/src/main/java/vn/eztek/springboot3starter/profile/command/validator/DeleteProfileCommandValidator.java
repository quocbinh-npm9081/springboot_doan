package vn.eztek.springboot3starter.profile.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.DeleteProfileCommand;
import vn.eztek.springboot3starter.profile.command.validated.DeleteProfileCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeleteProfileCommandValidator extends
    CommandValidation<DeleteProfileCommand, DeleteProfileCommandValidated> {

  private final UserRepository userRepository;

  @Override
  public DeleteProfileCommandValidated validate(DeleteProfileCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (loggedInUser.getDeletedAt() != null) {
      throw new BadRequestException("user-already-deleted");
    }

    return DeleteProfileCommandValidated.validatedOf(loggedInUser);

  }
}