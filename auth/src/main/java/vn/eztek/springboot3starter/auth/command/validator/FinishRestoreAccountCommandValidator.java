package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishRestoreAccountCommand;
import vn.eztek.springboot3starter.auth.command.validated.FinishRestoreAccountCommandValidated;
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
public class FinishRestoreAccountCommandValidator extends
    CommandValidation<FinishRestoreAccountCommand, FinishRestoreAccountCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public FinishRestoreAccountCommandValidated validate(FinishRestoreAccountCommand command) {

    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.RESTORE_ACCOUNT)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    var existUser = userRepository.findById(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              existUser.getUsername()));
    }

    if (existUser.getDeletedAt() == null) {
      throw new BadRequestException(
          "user-with-email-[%s]-is-not-deleted".formatted(existUser.getUsername()));
    }

    return FinishRestoreAccountCommandValidated.validatedOf(existUser, key);
  }

}
