package vn.eztek.springboot3starter.auth.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class ResetPasswordCommand implements Command {

  String password;
  String key;

}
