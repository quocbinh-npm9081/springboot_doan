package vn.eztek.springboot3starter.profile.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.ChangePasswordCommand;
import vn.eztek.springboot3starter.profile.command.validated.ChangePasswordCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangePasswordCommandValidator extends
    CommandValidation<ChangePasswordCommand, ChangePasswordCommandValidated> {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;

  @Override
  public ChangePasswordCommandValidated validate(ChangePasswordCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    if (user.getDeletedAt() != null) {
      throw new BadRequestException("user-already-deleted");
    }

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userName, command.getOldPassword()));
    } catch (Exception e) {
      throw new BadRequestException("old-password-is-not-correct");
    }
    return ChangePasswordCommandValidated.validatedOf(user);
  }

}