package vn.eztek.springboot3starter.publicAccess.command;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.Gender;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class InvitationMoveOnCommand implements Command {

  public String key;
  public String firstName;
  public String lastName;
  public String password;
  public String phoneNumber;
  public Gender gender;

}
