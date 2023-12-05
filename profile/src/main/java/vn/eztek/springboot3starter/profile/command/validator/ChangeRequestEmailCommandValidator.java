package vn.eztek.springboot3starter.profile.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.RequestChangeEmailCommand;
import vn.eztek.springboot3starter.profile.command.validated.ChangeRequestEmailCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeRequestEmailCommandValidator extends
    CommandValidation<RequestChangeEmailCommand, ChangeRequestEmailCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public ChangeRequestEmailCommandValidated validate(RequestChangeEmailCommand command) {
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

    var oldKey = keyRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(user.getId(),
        DateUtils.currentZonedDateTime(), KeyType.REQUEST_CHANGE_EMAIL).orElse(null);
    return ChangeRequestEmailCommandValidated.validatedOf(user, oldKey);

  }
}