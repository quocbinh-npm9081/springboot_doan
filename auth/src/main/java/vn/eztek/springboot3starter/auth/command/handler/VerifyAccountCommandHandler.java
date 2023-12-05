package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.VerifyAccountCommand;
import vn.eztek.springboot3starter.auth.command.event.AccountVerifiedEvent;
import vn.eztek.springboot3starter.auth.command.validator.VerifyAccountCommandValidator;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.FinishVerifyAccountMessage;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class VerifyAccountCommandHandler implements
    CommandHandler<VerifyAccountCommand, EmptyCommandResult, AuthAggregateId> {

  private final VerifyAccountCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final KeyRepository keyRepository;
  private final RedisMessagePublisher messagePublisher;

  @Override
  @Transactional
  public EmptyCommandResult handle(VerifyAccountCommand command, AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var key = validated.getKey();
    key.setUsed(true);
    keyRepository.save(key);

    // event storing
    var event = AccountVerifiedEvent.eventOf(authAggregateId,
        validated.getUser().getId().toString());
    eventStoreService.store(event);

    messagePublisher.publish(
        FinishVerifyAccountMessage.create(validated.getUser().getId().toString()));
    // resulting
    return EmptyCommandResult.empty();
  }

}
