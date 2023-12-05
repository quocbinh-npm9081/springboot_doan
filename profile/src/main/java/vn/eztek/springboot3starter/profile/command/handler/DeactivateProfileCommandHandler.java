package vn.eztek.springboot3starter.profile.command.handler;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.DeactivateProfileCommand;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeactivatedEvent;
import vn.eztek.springboot3starter.profile.command.validator.DeactivateProfileCommandValidator;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class DeactivateProfileCommandHandler implements
    CommandHandler<DeactivateProfileCommand, EmptyCommandResult, ProfileAggregateId> {

  private final DeactivateProfileCommandValidator validator;
  private final UserRepository userRepository;
  private final ProfileEventStoreService eventStoreService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Override
  public EmptyCommandResult handle(DeactivateProfileCommand command, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = validated.getUser();
    user.setStatus(UserStatus.SAFE_DISABLE);
    var updatedUser = userRepository.save(user);

    // event storing
    var event = ProfileDeactivatedEvent.eventOf(entityId, updatedUser.getId().toString());
    eventStoreService.store(event);

    // SEND MAIL ANNOUNCE TO USER
    var link =
        sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getActiveAccount();
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", user.getUsername());

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getNotificationDisableAccount(), templateData));

    // resulting
    return EmptyCommandResult.empty();
  }
}
