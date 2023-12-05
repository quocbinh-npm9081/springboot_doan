package vn.eztek.springboot3starter.user.command.handler;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;
import vn.eztek.springboot3starter.user.command.CreateUserCommand;
import vn.eztek.springboot3starter.user.command.event.UserCreatedEvent;
import vn.eztek.springboot3starter.user.command.validator.CreateUserCommandValidator;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.service.UserEventStoreService;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandler implements
    CommandHandler<CreateUserCommand, UserResponse, UserAggregateId> {

  private final CreateUserCommandValidator validator;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserEventStoreService eventStoreService;
  private final KeyRepository keyRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  Boolean enableKeyEmulator;

  @Override
  @Transactional
  public UserResponse handle(CreateUserCommand command, UserAggregateId userAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var user = userMapper.mapToUserBeforeCreate(command,
        passwordEncoder.encode(command.getPassword()), true, validated.getRole(),
        validated.getPrivileges());
    var createdUser = userRepository.save(user);

    var generatedKey = GeneratorUtils.generateKey();
    var key = userMapper.mapToKey(user, generatedKey, KeyType.ADMIN_CREATE);
    var createdKey = keyRepository.save(key);

    // storing event
    var event = UserCreatedEvent.eventOf(userAggregateId, createdUser.getId().toString(),
        createdUser.getFirstName(), createdUser.getLastName(), createdUser.getUsername(),
        command.getGender(), command.getPhoneNumber(), command.getRole(), command.getPrivileges());
    eventStoreService.store(event);

    //send event to queue
    var link =
        sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getRegistration()
            + createdKey.getKey();
    var templateData = new HashMap<String, String>();
    templateData.put("name", createdUser.getFirstName() + " " + createdUser.getLastName());
    templateData.put("link", link);
    templateData.put("currentDate",
        DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
    templateData.put("email", createdUser.getUsername());
    templateData.put("password", createdUser.getPassword());

    redisMessagePublisher.publish(SendMailMessage.create(createdUser.getUsername(),
        sendGridProperties.getDynamicTemplateId().getRegistration(), templateData));

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(List.of(
          SendEmulator.create(link, createdUser.getUsername(), EventAction.CREATE_USER.name(),
              createdKey.getKey()))));
    }

    // returning
    return userMapper.mapToUserResponse(user, null);
  }

}
