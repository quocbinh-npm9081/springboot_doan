package vn.eztek.springboot3starter.shared.cqrs;

import lombok.Value;

@Value(staticConstructor = "empty")
public class EmptyCommandResult implements CommandResult {

}
