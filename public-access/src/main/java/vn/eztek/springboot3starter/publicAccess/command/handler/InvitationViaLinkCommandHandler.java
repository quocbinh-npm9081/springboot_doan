package vn.eztek.springboot3starter.publicAccess.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.VerifyAccountMessage;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;
import vn.eztek.springboot3starter.domain.key.repository.KeyRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.publicAccess.command.InvitationViaLinkCommand;
import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationViaLinkEvent;
import vn.eztek.springboot3starter.publicAccess.command.validator.InvitationViaLinkCommandValidator;
import vn.eztek.springboot3starter.publicAccess.mapper.PublicAccessMapper;
import vn.eztek.springboot3starter.publicAccess.response.InvitationViaLinkResponse;
import vn.eztek.springboot3starter.publicAccess.service.PublicAccessEventService;
import vn.eztek.springboot3starter.publicAccess.vo.PublicAccessAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class InvitationViaLinkCommandHandler implements
    CommandHandler<InvitationViaLinkCommand, InvitationViaLinkResponse, PublicAccessAggregateId> {

  private final InvitationViaLinkCommandValidator validator;
  private final PublicAccessMapper publicAccessMapper;
  private final PublicAccessEventService eventStoreService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserProjectRepository userProjectRepository;
  private final RedisMessagePublisher messagePublisher;
  private final SendGridProperties sendGridProperties;
  private final KeyRepository keyRepository;


  @Override
  public InvitationViaLinkResponse handle(InvitationViaLinkCommand command,
      PublicAccessAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var user = publicAccessMapper.mapToUserBeforeCreateAndJoin(command, validated.getRoleTalent(),
        validated.getPrivilegeTalents(), passwordEncoder.encode(command.getPassword()),
        UserStatus.ACTIVE);
    var savedUser = userRepository.save(user);

    var member = publicAccessMapper.mapToUserProjectBeforeCreate(savedUser,
        validated.getInvitation().getProject(), UserProjectStatus.JOINED);
    userProjectRepository.save(member);

    var keyRandom = GeneratorUtils.generateKey();
    var key = publicAccessMapper.mapToKey(savedUser, keyRandom, KeyType.VERIFY_ACCOUNT);
    keyRepository.save(key);

    var event = UserInvitationViaLinkEvent.eventOf(entityId, savedUser.getFirstName(),
        savedUser.getLastName(), savedUser.getId().toString(), savedUser.getGender(),
        savedUser.getPhoneNumber());
    eventStoreService.store(event);

    // CREATE JOB UPDATE STATUS USER AND SEND EMAIL
    messagePublisher.publish(
        VerifyAccountMessage.create(savedUser.getFirstName(), savedUser.getLastName(),
            savedUser.getUsername(), savedUser.getId().toString(), keyRandom));

    // TODO CREATE NOTIFICATION
    // resulting
    return new InvitationViaLinkResponse(validated.getInvitation().getProject().getId());
  }

}
