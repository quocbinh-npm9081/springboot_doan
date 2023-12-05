package vn.eztek.springboot3starter.task.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class UserResponse implements CommandResult {

  UUID id;
  String firstName;
  String lastName;
  String email;
  String phoneNumber;
}
