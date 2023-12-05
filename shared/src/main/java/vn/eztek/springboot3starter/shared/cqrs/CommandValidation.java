package vn.eztek.springboot3starter.shared.cqrs;

public abstract class CommandValidation<C extends Command, CV extends CommandValidated> {

  public abstract CV validate(C command);

}
