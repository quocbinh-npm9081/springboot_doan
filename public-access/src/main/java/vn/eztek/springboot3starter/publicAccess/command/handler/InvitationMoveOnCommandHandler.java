package vn.eztek.springboot3starter.publicAccess.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.publicAccess.command.InvitationMoveOnCommand;
import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationMoveOnEvent;
import vn.eztek.springboot3starter.publicAccess.command.validator.InvitationMoveOnCommandValidator;
import vn.eztek.springboot3starter.publicAccess.mapper.PublicAccessMapper;
import vn.eztek.springboot3starter.publicAccess.service.PublicAccessEventService;
import vn.eztek.springboot3starter.publicAccess.vo.PublicAccessAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class InvitationMoveOnCommandHandler implements
    CommandHandler<InvitationMoveOnCommand, EmptyCommandResult,
        PublicAccessAggregateId> {

  private final InvitationMoveOnCommandValidator validator;
  private final PublicAccessMapper publicAccessMapper;
  private final PublicAccessEventService eventStoreService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public EmptyCommandResult handle(InvitationMoveOnCommand command,
      PublicAccessAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var user = publicAccessMapper.mapToUserBeforeMovingOnInvitation(validated.getUser(), command,
        passwordEncoder.encode(command.getPassword()), UserStatus.ACTIVE);
    var savedUser = userRepository.save(user);

    var event = UserInvitationMoveOnEvent.eventOf(entityId,
        savedUser.getFirstName(),
        savedUser.getLastName(),
        savedUser.getId().toString(),
        savedUser.getGender(),
        savedUser.getPhoneNumber());
    eventStoreService.store(event);

    // resulting
    return EmptyCommandResult.empty();
  }

}
