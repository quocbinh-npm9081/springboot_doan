package vn.eztek.springboot3starter.profile.command.handler;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.profile.command.RequestChangeEmailCommand;
import vn.eztek.springboot3starter.profile.command.event.EmailRequestChangedEvent;
import vn.eztek.springboot3starter.profile.command.validator.ChangeRequestEmailCommandValidator;
import vn.eztek.springboot3starter.profile.mapper.ProfileMapper;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class RequestChangeEmailCommandHandler implements
    CommandHandler<RequestChangeEmailCommand, EmptyCommandResult, ProfileAggregateId> {

  private final ChangeRequestEmailCommandValidator validator;
  private final KeyRepository keyRepository;
  private final ProfileEventStoreService eventStoreService;
  private final ProfileMapper profileMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  Boolean enableKeyEmulator;

  @Override
  public EmptyCommandResult handle(RequestChangeEmailCommand command, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = validated.getUser();
    var keyRandom = GeneratorUtils.generateKey();

    if (validated.getOldKey() != null) {
      // mark oldKey as used
      var key = validated.getOldKey();
      key.setUsed(true);
      keyRepository.save(key);
    }

    var key = profileMapper.mapToKey(user, keyRandom, KeyType.REQUEST_CHANGE_EMAIL);
    keyRepository.save(key);

    // event storing
    var event = EmailRequestChangedEvent.eventOf(entityId, validated.getUser().getId().toString());
    eventStoreService.store(event);

    //send event to queue
    var link =
        sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getChangeEmail()
            + keyRandom;
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", user.getUsername());

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getChangeEmail(), templateData));

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(List.of(
          SendEmulator.create(link, user.getUsername(), EventAction.REQUEST_CHANGE_EMAIL.name(),
              keyRandom))));
    }

    // resulting
    return EmptyCommandResult.empty();
  }
}
