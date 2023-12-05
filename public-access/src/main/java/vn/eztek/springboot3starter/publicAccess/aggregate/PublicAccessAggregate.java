package vn.eztek.springboot3starter.publicAccess.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.publicAccess.command.InvitationMoveOnCommand;
import vn.eztek.springboot3starter.publicAccess.command.InvitationViaLinkCommand;
import vn.eztek.springboot3starter.publicAccess.command.handler.InvitationMoveOnCommandHandler;
import vn.eztek.springboot3starter.publicAccess.command.handler.InvitationViaLinkCommandHandler;
import vn.eztek.springboot3starter.publicAccess.query.GetProjectByKeyQuery;
import vn.eztek.springboot3starter.publicAccess.query.handler.GetProjectByKeyQueryHandler;
import vn.eztek.springboot3starter.publicAccess.vo.PublicAccessAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class PublicAccessAggregate extends
    AggregateRoot<PublicAccessAggregate, PublicAccessAggregateId> {

  public PublicAccessAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new PublicAccessAggregateId());
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();
    // invitation move on
    behaviorBuilder.setCommandHandler(InvitationMoveOnCommand.class,
        getCommandHandler(InvitationMoveOnCommandHandler.class));
    behaviorBuilder.setCommandHandler(InvitationViaLinkCommand.class,
        getCommandHandler(InvitationViaLinkCommandHandler.class));

    // query
    behaviorBuilder.setQueryHandler(GetProjectByKeyQuery.class,
        getQueryHandler(GetProjectByKeyQueryHandler.class));

    return behaviorBuilder.build();
  }

  @Override
  public boolean sameIdentityAs(PublicAccessAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public PublicAccessAggregateId id() {
    return entityId;
  }
}
