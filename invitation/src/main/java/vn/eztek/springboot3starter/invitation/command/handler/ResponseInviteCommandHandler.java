package vn.eztek.springboot3starter.invitation.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.command.event.InviteResponsedEvent;
import vn.eztek.springboot3starter.invitation.command.validator.ResponseInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class ResponseInviteCommandHandler implements
    CommandHandler<ResponseInviteCommand, EmptyCommandResult, InvitationAggregateId> {

  private final ResponseInviteCommandValidator validator;
  private final InvitationMapper invitationMapper;
  private final UserProjectRepository userProjectRepository;
  private final InvitationRepository invitationRepository;
  private final InvitationEventService eventStoreService;

  @Override
  public EmptyCommandResult handle(ResponseInviteCommand command, InvitationAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var key = validated.getInvitation();
    key.setUsed(true);
    invitationRepository.save(key);
    if (command.getAccept()) {
      var userProject = invitationMapper.mapToUserProjectBeforeCreate(validated.getLoggedInUser(),
          validated.getProject(), UserProjectStatus.JOINED);
      userProjectRepository.save(userProject);
    }

    // storing event
    var event = InviteResponsedEvent.eventOf(entityId,
        validated.getLoggedInUser().getId().toString(), command.getAccept());
    eventStoreService.store(event);
    // resulting
    return EmptyCommandResult.empty();
  }
}
