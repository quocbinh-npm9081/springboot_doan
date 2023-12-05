package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ActivateAccountCommand;
import vn.eztek.springboot3starter.auth.command.event.AccountActivatedEvent;
import vn.eztek.springboot3starter.auth.command.validator.ActivateAccountCommandValidator;
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
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class ActivateAccountCommandHandler implements
    CommandHandler<ActivateAccountCommand, EmptyCommandResult, AuthAggregateId> {

  private final ActivateAccountCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final AuthMapper authMapper;
  private final KeyRepository keyRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  private Boolean enableKeyEmulator;

  @Override
  @Transactional
  public EmptyCommandResult handle(ActivateAccountCommand command,
      AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = validated.getUser();
    var key = validated.getOldKey();

    if (key != null) {
      key.setUsed(true);
      keyRepository.save(key);
    }

    var keyRandom = GeneratorUtils.generateKey();
    key = authMapper.mapToKey(user, keyRandom, KeyType.ACTIVE_ACCOUNT);
    keyRepository.save(key);

    // event storing
    var event = AccountActivatedEvent.eventOf(authAggregateId, user.getId().toString());
    eventStoreService.store(event);

    //send event to queue
    var link = sendGridProperties.getClient().getUri() + sendGridProperties.getPath()
        .getConfirmActiveAccount().formatted(keyRandom);

    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", user.getUsername());

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getActiveAccount(), templateData));

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(List.of(
          SendEmulator.create(link, user.getUsername(), KeyType.ACTIVE_ACCOUNT.name(),
              keyRandom))));
    }

    // resulting
    return EmptyCommandResult.empty();
  }

}
