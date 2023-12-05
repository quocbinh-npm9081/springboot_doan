package vn.eztek.springboot3starter.publicAccess.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.publicAccess.vo.PublicAccessAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class UserInvitationViaLinkEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;

  PublicAccessAggregateId id;
  String userId;
  String firstName;
  String lastName;
  Gender gender;
  String phoneNumber;

}
