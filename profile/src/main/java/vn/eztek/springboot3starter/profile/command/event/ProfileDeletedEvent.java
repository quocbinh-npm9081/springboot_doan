package vn.eztek.springboot3starter.profile.command.event;

import java.io.Serial;
import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.Event;

@Value(staticConstructor = "eventOf")
public class ProfileDeletedEvent implements Event {

  @Serial
  private static final long serialVersionUID = 1L;
  ProfileAggregateId id;
  String userId;
  String firstName;
  String lastName;
  String phoneNumber;
  Gender gender;
}
