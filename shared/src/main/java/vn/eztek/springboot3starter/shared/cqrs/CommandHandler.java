package vn.eztek.springboot3starter.shared.cqrs;

public interface CommandHandler<C extends Command, R extends CommandResult, ID> {

  R handle(C command, ID entityId);

}
