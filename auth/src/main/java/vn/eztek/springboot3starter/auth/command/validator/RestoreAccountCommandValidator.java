package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.RestoreAccountCommand;
import vn.eztek.springboot3starter.auth.command.validated.RestoreAccountCommandValidated;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RestoreAccountCommandValidator
    extends CommandValidation<RestoreAccountCommand, RestoreAccountCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public RestoreAccountCommandValidated validate(RestoreAccountCommand command) {
    var existUser = userRepository.findByUsernameIgnoreCase(command.getEmail())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              command.getEmail()));
    }

    if (existUser.getDeletedAt() == null) {
      throw new BadRequestException(
          "user-with-email-[%s]-is-not-deleted".formatted(command.getEmail()));
    }

    var oldKey = keyRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(
        existUser.getId(),
        DateUtils.currentZonedDateTime(), KeyType.RESTORE_ACCOUNT).orElse(null);

    return RestoreAccountCommandValidated.validatedOf(existUser, oldKey);
  }

}
