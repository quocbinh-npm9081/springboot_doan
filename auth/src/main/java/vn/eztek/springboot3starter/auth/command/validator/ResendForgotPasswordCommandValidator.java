package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ResendForgotPasswordCommand;
import vn.eztek.springboot3starter.auth.command.validated.ResendForgotPasswordCommandValidated;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ResendForgotPasswordCommandValidator
    extends CommandValidation<ResendForgotPasswordCommand, ResendForgotPasswordCommandValidated> {

  private final KeyRepository keyRepository;

  @Override
  public ResendForgotPasswordCommandValidated validate(ResendForgotPasswordCommand command) {
    var key = keyRepository
        .findByKeyAndUsedFalseAndAction(command.getKey(), KeyType.FORGOT_PASSWORD)
        .orElseThrow(() -> new BadRequestException("invalid-key"));

    return ResendForgotPasswordCommandValidated.validatedOf(key);
  }

}
