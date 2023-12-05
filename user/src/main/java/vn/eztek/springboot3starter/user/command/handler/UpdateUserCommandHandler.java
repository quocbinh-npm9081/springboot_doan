package vn.eztek.springboot3starter.user.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.user.command.UpdateUserCommand;
import vn.eztek.springboot3starter.user.command.event.UserUpdatedEvent;
import vn.eztek.springboot3starter.user.command.validator.UpdateUserCommandValidator;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.service.UserEventStoreService;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler
    implements CommandHandler<UpdateUserCommand, UserResponse, UserAggregateId> {

  private final UpdateUserCommandValidator validator;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserEventStoreService eventStoreService;
  private final EventStoreRepository eventStoreRepository;

  @Override
  @Transactional
  public UserResponse handle(UpdateUserCommand command, UserAggregateId userAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var user = userMapper.mapToUserBeforeUpdate(validated.getUser(), command, validated.getRole(),
        validated.getPrivileges());
    var updateUser = userRepository.save(user);

    var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.SIGN_IN,
        updateUser.getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
    var lastSignedInTime = eventStore.map(
        store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);

    // event storing
    var event = UserUpdatedEvent.eventOf(userAggregateId, updateUser.getId().toString(),
        updateUser.getFirstName(), updateUser.getLastName(), command.getRole(),
        command.getPrivileges());
    eventStoreService.store(event);

    // returning
    return userMapper.mapToUserResponse(user, lastSignedInTime);
  }

}
