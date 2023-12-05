package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.event.UserChangeEmailEvent;
import vn.eztek.springboot3starter.auth.command.validator.ChangeEmailCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class ChangeEmailCommandHandler implements
    CommandHandler<ChangeEmailCommand, EmptyCommandResult, AuthAggregateId> {

  private final ChangeEmailCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final AuthMapper authMapper;
  private final KeyRepository keyRepository;
  private final UserRepository userRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  private Boolean enableKeyEmulator;

  @Override
  @Transactional
  public EmptyCommandResult handle(ChangeEmailCommand command, AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = authMapper.mapToUserBeforeChangeEmail(validated.getUser(), command.getUsername(),
        UserStatus.INACTIVE);
    var saveUser = userRepository.save(user);

    var key = validated.getKey();
    key.setUsed(true);
    keyRepository.save(key);

    // send mail validation
    var keyRandom = GeneratorUtils.generateKey();
    if (validated.getOldKey() != null) {
      // mark oldKey as used
      key = validated.getOldKey();
      key.setUsed(true);
      keyRepository.save(key);
    }

    key = authMapper.mapToKey(user, keyRandom, KeyType.UPDATE_STATUS_AFTER_CHANGE_EMAIL);
    keyRepository.save(key);
    // event storing
    var event = UserChangeEmailEvent.eventOf(authAggregateId, saveUser.getId().toString(),
        saveUser.getUsername());
    eventStoreService.store(event);

    //send event to queue
    var link = sendGridProperties.getClient().getUri() + sendGridProperties.getPath()
        .getConfirmAfterChangeEmail() + keyRandom;
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", user.getUsername());

    redisMessagePublisher.publish(
        SendMailMessage.create(user.getUsername(),
            sendGridProperties.getDynamicTemplateId().getConfirmAfterChangeEmail(), templateData));

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(List.of(
          SendEmulator.create(link, user.getUsername(),
              KeyType.UPDATE_STATUS_AFTER_CHANGE_EMAIL.toString(),
              keyRandom))));
    }

    // resulting
    return EmptyCommandResult.empty();
  }

}
