package vn.eztek.springboot3starter.profile.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.DeactivateProfileCommand;
import vn.eztek.springboot3starter.profile.command.validated.DeactivateProfileCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeactivateProfileCommandValidator extends
    CommandValidation<DeactivateProfileCommand, DeactivateProfileCommandValidated> {

  private final UserRepository userRepository;

  @Override
  public DeactivateProfileCommandValidated validate(DeactivateProfileCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    return DeactivateProfileCommandValidated.validatedOf(user);

  }
}