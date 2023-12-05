package vn.eztek.springboot3starter.profile.command.handler;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.ChangePasswordCommand;
import vn.eztek.springboot3starter.profile.command.event.PasswordChangedEvent;
import vn.eztek.springboot3starter.profile.command.validator.ChangePasswordCommandValidator;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class ChangePasswordCommandHandler implements
    CommandHandler<ChangePasswordCommand, EmptyCommandResult, ProfileAggregateId> {

  private final ChangePasswordCommandValidator validator;
  private final UserRepository userRepository;
  private final ProfileEventStoreService eventStoreService;
  private final PasswordEncoder passwordEncoder;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Override
  public EmptyCommandResult handle(ChangePasswordCommand command, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(command);
    var user = validated.getUser();

    validated.getUser().setPassword(passwordEncoder.encode(command.getNewPassword()));
    userRepository.save(validated.getUser());

    // event storing
    var event = PasswordChangedEvent.eventOf(entityId, validated.getUser().getId().toString());
    eventStoreService.store(event);

    //send event to queue
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());

    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getChangePassword(), templateData));

    // resulting
    return EmptyCommandResult.empty();
  }
}
