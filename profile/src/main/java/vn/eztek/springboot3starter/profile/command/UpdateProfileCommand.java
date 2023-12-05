package vn.eztek.springboot3starter.profile.command;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateProfileCommand implements Command {

  String firstName;
  String lastName;
  String phoneNumber;
  Gender gender;
}
