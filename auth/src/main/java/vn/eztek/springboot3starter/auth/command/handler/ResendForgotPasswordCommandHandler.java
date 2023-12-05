package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.ResendForgotPasswordCommand;
import vn.eztek.springboot3starter.auth.command.event.UserResendForgotPasswordEvent;
import vn.eztek.springboot3starter.auth.command.validator.ResendForgotPasswordCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class ResendForgotPasswordCommandHandler implements
    CommandHandler<ResendForgotPasswordCommand, EmptyCommandResult, AuthAggregateId> {

  private final ResendForgotPasswordCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final KeyRepository keyRepository;
  private final AuthMapper authMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  private Boolean enableKeyEmulator;

  @Override
  @Transactional
  public EmptyCommandResult handle(ResendForgotPasswordCommand command,
      AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = validated.getExpiredKey().getUser();
    var keyRandom = GeneratorUtils.generateKey();
    var keyExpiredTime = validated.getExpiredKey();
    keyExpiredTime.setUsed(true);
    keyRepository.save(keyExpiredTime);

    var key = authMapper.mapToKey(user, keyRandom, KeyType.FORGOT_PASSWORD);
    keyRepository.save(key);

    // event storing
    var event = UserResendForgotPasswordEvent.eventOf(authAggregateId, user.getId().toString(),
        user.getUsername());
    eventStoreService.store(event);

    // send message to queue
    var link =
        sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getResetPassword()
            + keyRandom;
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", user.getUsername());

    redisMessagePublisher.publish(SendMailMessage.create(user.getUsername(),
        sendGridProperties.getDynamicTemplateId().getForgotPassword(), templateData));

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(List.of(
          SendEmulator.create(link, user.getUsername(),
              EventAction.RESEND_FORGOT_PASSWORD.toString(),
              keyRandom))));
    }

    // resulting
    return EmptyCommandResult.empty();
  }
}
