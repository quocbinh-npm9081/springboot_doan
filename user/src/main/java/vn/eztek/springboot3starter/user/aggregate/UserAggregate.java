package vn.eztek.springboot3starter.user.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;
import vn.eztek.springboot3starter.user.command.CreateUserCommand;
import vn.eztek.springboot3starter.user.command.UpdateUserCommand;
import vn.eztek.springboot3starter.user.command.UpdateUserStatusCommand;
import vn.eztek.springboot3starter.user.command.handler.CreateUserCommandHandler;
import vn.eztek.springboot3starter.user.command.handler.UpdateUserCommandHandler;
import vn.eztek.springboot3starter.user.command.handler.UpdateUserStatusCommandHandler;
import vn.eztek.springboot3starter.user.query.AutoCompleteSearchQuery;
import vn.eztek.springboot3starter.user.query.GetUserByIdQuery;
import vn.eztek.springboot3starter.user.query.ListUsersQuery;
import vn.eztek.springboot3starter.user.query.handler.AutoCompleteSearchQueryHandler;
import vn.eztek.springboot3starter.user.query.handler.GetUserByIdQueryHandler;
import vn.eztek.springboot3starter.user.query.handler.ListUsersQueryHandler;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

public class UserAggregate
    extends AggregateRoot<UserAggregate, UserAggregateId> {

  public UserAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new UserAggregateId());
  }

  public UserAggregate(ApplicationContext applicationContext,
      UserAggregateId userAggregateId) {
    super(applicationContext, userAggregateId);
  }

  @Override
  public boolean sameIdentityAs(UserAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public UserAggregateId id() {
    return entityId;
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    // command
    behaviorBuilder.setCommandHandler(CreateUserCommand.class,
        getCommandHandler(CreateUserCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateUserCommand.class,
        getCommandHandler(UpdateUserCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateUserStatusCommand.class,
        getCommandHandler(UpdateUserStatusCommandHandler.class));

    // query
    behaviorBuilder.setQueryHandler(GetUserByIdQuery.class,
        getQueryHandler(GetUserByIdQueryHandler.class));
    behaviorBuilder.setQueryHandler(ListUsersQuery.class,
        getQueryHandler(ListUsersQueryHandler.class));
    behaviorBuilder.setQueryHandler(AutoCompleteSearchQuery.class,
        getQueryHandler(AutoCompleteSearchQueryHandler.class));

    return behaviorBuilder.build();
  }

}
