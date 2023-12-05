package vn.eztek.springboot3starter.auth.command.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.FinishRestoreAccountCommand;
import vn.eztek.springboot3starter.auth.command.event.AccountRestoreFinishedEvent;
import vn.eztek.springboot3starter.auth.command.validator.FinishRestoreAccountCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.AccountRestoreMessage;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class FinishRestoreAccountCommandHandler implements
    CommandHandler<FinishRestoreAccountCommand, EmptyCommandResult, AuthAggregateId> {

  private final FinishRestoreAccountCommandValidator validator;
  private final AuthMapper authMapper;
  private final AuthEventStoreService eventStoreService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final KeyRepository keyRepository;

  @Override
  public EmptyCommandResult handle(FinishRestoreAccountCommand command, AuthAggregateId entityId) {

    var validated = validator.validate(command);

    // handling
    var user = authMapper.mapToUserBeforeUpdateDeleteAt(validated.getUser(), null);
    var saveUser = userRepository.save(user);

    var key = validated.getKey();
    key.setUsed(true);
    keyRepository.save(key);

    // store event
    var event = AccountRestoreFinishedEvent.eventOf(entityId,
        validated.getUser().getId().toString());
    eventStoreService.store(event);

    // send event to queue
    List<UserProjectMessage> userProfiles = userProjectRepository.findByProjectOwnerIdAndUserIdNot(
            saveUser.getId(), validated.getUser().getId()).stream()
        .map(authMapper::mapToUserProjectMessage).toList();

    redisMessagePublisher.publish(
        AccountRestoreMessage.create(validated.getUser().getId(), validated.getUser().getUsername(),
            validated.getUser().getFirstName() + " " + validated.getUser().getLastName(),
            userProfiles));

    // resulting
    return EmptyCommandResult.empty();
  }

}
