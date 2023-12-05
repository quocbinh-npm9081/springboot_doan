package vn.eztek.springboot3starter.invitation.command.event;

import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class TalentInviteEvent implements Event {

  InvitationAggregateId id;
  String userId;
  List<String> emails;

}
