package vn.eztek.springboot3starter.invitation.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.invitation.command.CheckUserMatchInviteCommand;
import vn.eztek.springboot3starter.invitation.command.event.UserMatchInviteCheckedEvent;
import vn.eztek.springboot3starter.invitation.command.validator.CheckUserMatchInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.response.CheckUserMatchInviteResponse;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class CheckUserMatchInviteCommandHandler implements
    CommandHandler<CheckUserMatchInviteCommand, CheckUserMatchInviteResponse, InvitationAggregateId> {

  private final CheckUserMatchInviteCommandValidator validator;
  private final InvitationMapper invitationMapper;
  private final InvitationEventService eventStoreService;

  @Override
  public CheckUserMatchInviteResponse handle(CheckUserMatchInviteCommand command,
      InvitationAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var res = validated.getUser().getId() == validated.getInvitation().getUser().getId();

    // storing event
    var event = UserMatchInviteCheckedEvent.eventOf(entityId,
        validated.getUser().getId().toString(), validated.getInvitation().getId().toString(), res);
    eventStoreService.store(event);
    // resulting
    return invitationMapper.mapToCheckResponse(res);
  }
}
