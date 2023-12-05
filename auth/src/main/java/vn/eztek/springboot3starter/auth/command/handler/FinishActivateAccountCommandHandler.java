package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishActivateAccountCommand;
import vn.eztek.springboot3starter.auth.command.event.ActivateAccountFinishedEvent;
import vn.eztek.springboot3starter.auth.command.validator.FinishActivateAccountCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class FinishActivateAccountCommandHandler implements
    CommandHandler<FinishActivateAccountCommand, EmptyCommandResult, AuthAggregateId> {

  private final FinishActivateAccountCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final AuthMapper authMapper;
  private final KeyRepository keyRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public EmptyCommandResult handle(FinishActivateAccountCommand command,
      AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = authMapper.mapToUserBeforeUpdateStatus(validated.getUser(), UserStatus.ACTIVE);
    var saveUser = userRepository.save(user);

    var key = validated.getKey();
    key.setUsed(true);
    keyRepository.save(key);

    // event storing
    var event = ActivateAccountFinishedEvent.eventOf(authAggregateId, saveUser.getId().toString());
    eventStoreService.store(event);

    // resulting
    return EmptyCommandResult.empty();
  }

}
