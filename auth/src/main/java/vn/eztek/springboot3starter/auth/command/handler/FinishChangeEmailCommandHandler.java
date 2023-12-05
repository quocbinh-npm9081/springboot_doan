package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.event.UserChangeEmailFinishedEvent;
import vn.eztek.springboot3starter.auth.command.validator.FinishChangeEmailCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;


@Component
@RequiredArgsConstructor
public class FinishChangeEmailCommandHandler
    implements CommandHandler<FinishChangeEmailCommand, EmptyCommandResult, AuthAggregateId> {

  private final FinishChangeEmailCommandValidator validator;
  private final UserRepository userRepository;
  private final AuthMapper authMapper;
  private final AuthEventStoreService eventStoreService;


  @Override
  @Transactional
  public EmptyCommandResult handle(FinishChangeEmailCommand command,
      AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = authMapper.mapToUserBeforeUpdateStatus(validated.getUser(), UserStatus.ACTIVE);
    var userUpdate = userRepository.save(user);

    // event storing
    var event = UserChangeEmailFinishedEvent.eventOf(authAggregateId, UserStatus.ACTIVE,
        user.getId().toString());
    eventStoreService.store(event);

    // resulting
    return EmptyCommandResult.empty();
  }
}
