package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.VerifyAccountCommand;
import vn.eztek.springboot3starter.auth.command.validated.VerifyAccountCommandValidated;
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
public class VerifyAccountCommandValidator extends
    CommandValidation<VerifyAccountCommand, VerifyAccountCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public VerifyAccountCommandValidated validate(VerifyAccountCommand command) {

    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.VERIFY_ACCOUNT)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    var existUser = userRepository.findById(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }

    if (existUser.getDeletedAt() != null) {
      throw new BadRequestException(
          "user-with-email-[%s]-is-deleted".formatted(existUser.getUsername()));
    }

    return VerifyAccountCommandValidated.validatedOf(existUser, key);
  }

}
