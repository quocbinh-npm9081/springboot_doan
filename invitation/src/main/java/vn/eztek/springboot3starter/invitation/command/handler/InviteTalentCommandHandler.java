package vn.eztek.springboot3starter.invitation.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulator;
import vn.eztek.springboot3starter.common.redis.messages.SendEmulatorMessage;
import vn.eztek.springboot3starter.common.redis.messages.SendMailMessage;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.event.TalentInviteEvent;
import vn.eztek.springboot3starter.invitation.command.validator.InviteTalentCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class InviteTalentCommandHandler implements
    CommandHandler<InviteTalentCommand, EmptyCommandResult, InvitationAggregateId> {

  private final InviteTalentCommandValidator validator;
  private final InvitationMapper invitationMapper;
  private final UserRepository userRepository;
  private final InvitationEventService eventStoreService;
  private final InvitationRepository invitationRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final SendGridProperties sendGridProperties;

  @Value("${key-emulator}")
  Boolean enableKeyEmulator;

  @Override
  public EmptyCommandResult handle(InviteTalentCommand command, InvitationAggregateId entityId) {
    // validating
    var validated = validator.validate(command);
    Map<User, String> map = new HashMap<>();

    // handling
    for (Map.Entry<String, User> set : validated.getUsers().entrySet()) {
      if (set.getValue() == null) {
        var userNotCreated = new User(set.getKey(), validated.getTalentRole(),
            validated.getPrivilegeTalents(), UserStatus.REGISTERING);
        userNotCreated = userRepository.save(userNotCreated);
        var keyRandom = GeneratorUtils.generateKey();
        var invitation = invitationMapper.mapToInvitation(userNotCreated,
            validated.getLoggedInUser(), validated.getProject(), keyRandom,
            InvitationType.INVITE_TALENT_BY_MAIL);
        invitationRepository.save(invitation);
        map.put(userNotCreated, keyRandom);
      } else {
        var oldKey = invitationRepository.findByUserIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(
                set.getValue().getId(), command.getProjectId(), DateUtils.currentZonedDateTime(),
                InvitationType.INVITE_TALENT_BY_MAIL)

            .orElse(null);
        var keyRandom = GeneratorUtils.generateKey();
        if (oldKey != null) {
          // mark oldKey as used
          oldKey.setUsed(true);
          invitationRepository.save(oldKey);
        }
        var invitation = invitationMapper.mapToInvitation(set.getValue(),
            validated.getLoggedInUser(), validated.getProject(), keyRandom,
            InvitationType.INVITE_TALENT_BY_MAIL);
        invitationRepository.save(invitation);
        map.put(set.getValue(), keyRandom);
      }
    }

    // storing event
    var event = TalentInviteEvent.eventOf(entityId, validated.getLoggedInUser().getId().toString(),
        command.getEmails());
    eventStoreService.store(event);

    // send message to queue
    List<SendEmulator> emulators = new ArrayList<>();
    for (Map.Entry<User, String> set : map.entrySet()) {
      var link =
          sendGridProperties.getClient().getUri() + sendGridProperties.getPath().getInviteTalent()
              + set.getValue();
      var user = validated.getLoggedInUser();
      var templateData = new HashMap<String, String>();
      templateData.put("agencyName", user.getFirstName() + " " + user.getLastName());
      templateData.put("projectName", validated.getProject().getName());
      templateData.put("link", link);
      templateData.put("currentDate",
          DateUtils.zonedDateTimeToString(DateUtils.currentZonedDateTime()));
      templateData.put("email", set.getKey().getUsername());

      redisMessagePublisher.publish(SendMailMessage.create(set.getKey().getUsername(),
          sendGridProperties.getDynamicTemplateId().getInviteTalent(), templateData));

      if (enableKeyEmulator) {
        emulators.add(
            SendEmulator.create(link, user.getUsername(), EventAction.INVITE_TALENT.name(),
                set.getValue()));
      }
    }

    if (enableKeyEmulator) {
      redisMessagePublisher.publish(SendEmulatorMessage.create(emulators));
    }
    // resulting
    return EmptyCommandResult.empty();
  }


}
