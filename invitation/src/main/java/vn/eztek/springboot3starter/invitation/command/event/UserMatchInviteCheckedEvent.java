package vn.eztek.springboot3starter.invitation.command.event;

import lombok.Value;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class UserMatchInviteCheckedEvent implements Event {

  InvitationAggregateId id;
  String userId;
  String invitationId;
  Boolean isMatch;

}
