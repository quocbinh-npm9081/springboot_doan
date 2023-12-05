package vn.eztek.springboot3starter.user.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.user.command.UpdateUserStatusCommand;
import vn.eztek.springboot3starter.user.command.validated.UpdateUserStatusCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateUserStatusCommandValidator extends
    CommandValidation<UpdateUserStatusCommand, UpdateUserStatusCommandValidated> {

  private final UserRepository userRepository;

  @Override
  public UpdateUserStatusCommandValidated validate(UpdateUserStatusCommand command) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    var user = userRepository.findByIdAndDeletedAtNull(command.getUserId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (user.getStatus().equals(command.getStatus())) {
      throw new BadRequestException("user-is-already-%s".formatted(command.getStatus()));
    }
    var role = user.getRole().getName();

    if (role.equals(RoleName.ADMINISTRATOR)) {
      throw new BadRequestException("administrator-or-talent-cannot-be-deactivated");
    }

    return UpdateUserStatusCommandValidated.validatedOf(user, loggedInUser);
  }

}
