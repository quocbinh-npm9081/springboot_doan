package vn.eztek.springboot3starter.profile.command;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class DeleteProfileCommand implements Command {

}
