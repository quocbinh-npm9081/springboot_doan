package vn.eztek.springboot3starter.user.command.handler;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.user.command.UpdateUserStatusCommand;
import vn.eztek.springboot3starter.user.command.event.UserStatusUpdatedEvent;
import vn.eztek.springboot3starter.user.command.validator.UpdateUserStatusCommandValidator;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.service.UserEventStoreService;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateUserStatusCommandHandler implements
    CommandHandler<UpdateUserStatusCommand, UserResponse, UserAggregateId> {

  private final UpdateUserStatusCommandValidator validator;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserEventStoreService eventStoreService;
  private final EventStoreRepository eventStoreRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Override
  @Transactional
  public UserResponse handle(UpdateUserStatusCommand command, UserAggregateId userAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = userMapper.mapToUserBeforeUpdateStatus(validated.getUser(), command.getStatus());
    var userUpdate = userRepository.save(user);

    // event storing
    var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.SIGN_IN,
        userUpdate.getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
    var lastSignedInTime = eventStore.map(
        store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);
    var event = UserStatusUpdatedEvent.eventOf(userAggregateId,
        validated.getLoggedInUser().getId().toString(), command.getStatus());
    eventStoreService.store(event);

    //send event to queue
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("status", command.getStatus().toString().toUpperCase());

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getNotificationStatus()
        , templateData
    ));

    // resulting
    return userMapper.mapToUserResponse(userUpdate, lastSignedInTime);
  }
}
