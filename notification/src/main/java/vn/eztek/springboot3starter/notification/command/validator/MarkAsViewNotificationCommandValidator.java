package vn.eztek.springboot3starter.notification.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.notification.command.MarkAsViewNotificationCommand;
import vn.eztek.springboot3starter.notification.command.validated.MarkAsViewNotificationCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MarkAsViewNotificationCommandValidator extends
    CommandValidation<MarkAsViewNotificationCommand, MarkAsViewNotificationCommandValidated> {

  private final UserRepository userRepository;

  @Override
  public MarkAsViewNotificationCommandValidated validate(MarkAsViewNotificationCommand command) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(authentication.getName())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }
    return MarkAsViewNotificationCommandValidated.validatedOf(user);
  }
}
