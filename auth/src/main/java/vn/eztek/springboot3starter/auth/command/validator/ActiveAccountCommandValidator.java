package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ActiveAccountCommand;
import vn.eztek.springboot3starter.auth.command.validated.ActiveAccountCommandValidated;
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
public class ActiveAccountCommandValidator
    extends CommandValidation<ActiveAccountCommand, ActiveAccountCommandValidated> {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;

  @Override
  public ActiveAccountCommandValidated validate(ActiveAccountCommand command) {
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(command.getEmail())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.SAFE_DISABLE)) {
      throw new BadRequestException("user-is-not-safe-disable");
    }

    var oldKey = keyRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(user.getId(),
        DateUtils.currentZonedDateTime(), KeyType.ACTIVE_ACCOUNT).orElse(null);

    return ActiveAccountCommandValidated.validatedOf(user, oldKey);
  }

}
