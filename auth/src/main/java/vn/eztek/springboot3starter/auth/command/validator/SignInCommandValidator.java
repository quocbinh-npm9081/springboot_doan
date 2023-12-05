package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.SignInCommand;
import vn.eztek.springboot3starter.auth.command.validated.SignInCommandValidated;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SignInCommandValidator extends
    CommandValidation<SignInCommand, SignInCommandValidated> {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;

  @Override
  public SignInCommandValidated validate(SignInCommand command) {
    var user = userRepository.findByUsernameContainingIgnoreCase(command.getUsername())
        .orElseThrow(() -> new BadRequestException("user-not-found"));

    if (user.getStatus().equals(UserStatus.SAFE_DISABLE)) {
      throw new BadRequestException("user-is-safe-disable");
    }

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }

    if (user.getDeletedAt() != null) {
      throw new BadRequestException("user-is-under-deletion");
    }

    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(command.getUsername(), command.getPassword()));

    return SignInCommandValidated.validatedOf(authentication);

  }

}
