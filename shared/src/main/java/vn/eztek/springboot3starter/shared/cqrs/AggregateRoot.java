package vn.eztek.springboot3starter.shared.cqrs;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public abstract class AggregateRoot<E, ID extends Serializable> implements Entity<E, ID> {

  public final ID entityId;
  private final ApplicationContext applicationContext;
  private final AggregateRootBehavior behavior;

  protected AggregateRoot(ApplicationContext applicationContext, ID entityId) {
    this.entityId = entityId;
    this.applicationContext = applicationContext;
    this.behavior = initialBehavior();
  }

  public <C extends Command, R extends CommandResult> R handle(C command) {
    var commandHandler = (CommandHandler<C, R, ID>) behavior.commandHandlers.get(
        command.getClass());
    return commandHandler.handle(command, entityId);
  }

  public <Q extends Query, R extends QueryResult> R handle(Q query) {
    var queryHandler = (QueryHandler<Q, R, ID>) behavior.queryHandlers.get(query.getClass());
    return queryHandler.handle(query, entityId);
  }

  protected <C extends Command, R extends CommandResult> CommandHandler<C, R, ID> getCommandHandler(
      Class<? extends CommandHandler> commandHandlerClass) {
    return applicationContext.getBean(commandHandlerClass);
  }

  protected <Q extends Query, R extends QueryResult> QueryHandler<Q, R, ID> getQueryHandler(
      Class<? extends QueryHandler> queryHandlerClass) {
    return applicationContext.getBean(queryHandlerClass);
  }

  protected abstract AggregateRootBehavior initialBehavior();

  public class AggregateRootBehavior<ID> {

    protected final Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends CommandResult, ID>> commandHandlers;
    protected final Map<Class<? extends Query>, QueryHandler<? extends Query, ? extends QueryResult, ID>> queryHandlers;

    public AggregateRootBehavior(
        Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends CommandResult, ID>> commandHandlers,
        Map<Class<? extends Query>, QueryHandler<? extends Query, ? extends QueryResult, ID>> queryHandlers) {
      this.commandHandlers = Collections.unmodifiableMap(commandHandlers);
      this.queryHandlers = Collections.unmodifiableMap(queryHandlers);
    }

  }

  public class AggregateRootBehaviorBuilder<ID> {

    private final Map<Class<? extends Command>, CommandHandler<? extends Command, ? extends CommandResult, ID>> commandHandlers =
        new HashMap<>();

    private final Map<Class<? extends Query>, QueryHandler<? extends Query, ? extends QueryResult, ID>> queryHandlers =
        new HashMap<>();

    public <C extends Command, R extends CommandResult> AggregateRootBehaviorBuilder<ID> setCommandHandler(
        Class<C> commandClass, CommandHandler<C, R, ID> handler) {
      commandHandlers.put(commandClass, handler);
      return this;
    }

    public <Q extends Query, R extends QueryResult> AggregateRootBehaviorBuilder<ID> setQueryHandler(
        Class<Q> queryClass, QueryHandler<Q, R, ID> handler) {
      queryHandlers.put(queryClass, handler);
      return this;
    }

    public AggregateRootBehavior<ID> build() {
      return new AggregateRootBehavior(commandHandlers, queryHandlers);
    }

  }

}
