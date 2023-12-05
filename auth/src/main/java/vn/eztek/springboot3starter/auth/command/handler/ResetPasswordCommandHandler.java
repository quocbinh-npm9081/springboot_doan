package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ResetPasswordCommand;
import vn.eztek.springboot3starter.auth.command.event.UserResetPasswordEvent;
import vn.eztek.springboot3starter.auth.command.validator.ResetPasswordCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class ResetPasswordCommandHandler
    implements CommandHandler<ResetPasswordCommand, EmptyCommandResult, AuthAggregateId> {

  private final ResetPasswordCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final AuthMapper authMapper;
  private final KeyRepository keyRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public EmptyCommandResult handle(ResetPasswordCommand command, AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var password = passwordEncoder.encode(command.getPassword());
    var user = authMapper.mapToUserBeforeResetPassword(validated.getUser(), password);
    var saveUser = userRepository.save(user);

    var key = validated.getKey();
    key.setUsed(true);
    keyRepository.save(key);

    // event storing
    var event = UserResetPasswordEvent.eventOf(authAggregateId, saveUser.getId().toString(),
        saveUser.getUsername());
    eventStoreService.store(event);

    // resulting
    return EmptyCommandResult.empty();
  }

}
