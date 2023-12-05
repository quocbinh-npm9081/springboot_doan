package vn.eztek.springboot3starter.invitation.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.invitation.command.CheckMemberProjectCommand;
import vn.eztek.springboot3starter.invitation.command.event.MemberProjectCheckedEvent;
import vn.eztek.springboot3starter.invitation.command.validator.CheckMemberProjectCommandValidator;
import vn.eztek.springboot3starter.invitation.response.CheckMemberProjectResponse;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class CheckMemberProjectCommandHandler implements
    CommandHandler<CheckMemberProjectCommand, CheckMemberProjectResponse, InvitationAggregateId> {

  private final CheckMemberProjectCommandValidator validator;
  private final InvitationEventService eventStoreService;

  @Override
  public CheckMemberProjectResponse handle(CheckMemberProjectCommand command,
      InvitationAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // storing event
    var event = MemberProjectCheckedEvent.eventOf(entityId, validated.getUser().getId().toString(),
        validated.getInvitation().getId().toString());
    eventStoreService.store(event);

    // resulting
    return CheckMemberProjectResponse.create(validated.getUserProject() != null);
  }
}
