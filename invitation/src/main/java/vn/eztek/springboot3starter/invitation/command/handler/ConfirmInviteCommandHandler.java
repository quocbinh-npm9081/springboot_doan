package vn.eztek.springboot3starter.invitation.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.event.InviteConfirmEvent;
import vn.eztek.springboot3starter.invitation.command.validator.ConfirmInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.response.ConfirmInviteResponse;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class ConfirmInviteCommandHandler implements
    CommandHandler<ConfirmInviteCommand, ConfirmInviteResponse, InvitationAggregateId> {

  private final ConfirmInviteCommandValidator validator;
  private final InvitationMapper invitationMapper;
  private final UserProjectRepository userProjectRepository;
  private final InvitationEventService eventStoreService;

  @Override
  public ConfirmInviteResponse handle(ConfirmInviteCommand command,
      InvitationAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    if (!validated.getIsMember()) {
      var userProject = invitationMapper.mapToUserProjectBeforeCreate(validated.getLoggedInUser(),
          validated.getProject(), UserProjectStatus.JOINED);
      userProjectRepository.save(userProject);
    }
    // storing event
    var event = InviteConfirmEvent.eventOf(entityId, validated.getLoggedInUser().getId().toString(),
        validated.getProject().getId().toString());
    eventStoreService.store(event);

    // resulting
    return ConfirmInviteResponse.create(validated.getProject().getId().toString());
  }
}
