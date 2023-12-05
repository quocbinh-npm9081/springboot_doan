package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishActivateAccountCommand;
import vn.eztek.springboot3starter.auth.command.validated.FinishActivateAccountCommandValidated;
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
public class FinishActivateAccountCommandValidator extends
    CommandValidation<FinishActivateAccountCommand, FinishActivateAccountCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public FinishActivateAccountCommandValidated validate(FinishActivateAccountCommand command) {

    var key = keyRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(command.getKey(),
            DateUtils.currentZonedDateTime(), KeyType.ACTIVE_ACCOUNT)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    var existUser = userRepository.findById(key.getUser().getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.SAFE_DISABLE)) {
      throw new BadRequestException("user-is-not-safe-disable");
    }

    if (existUser.getDeletedAt() != null) {
      throw new BadRequestException(
          "user-with-email-[%s]-is-not-deleted".formatted(existUser.getUsername()));
    }

    return FinishActivateAccountCommandValidated.validatedOf(existUser, key);
  }

}
