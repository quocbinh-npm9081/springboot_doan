package vn.eztek.springboot3starter.auth.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class CheckRegisteringUserCommand implements Command {

  String key;
}
