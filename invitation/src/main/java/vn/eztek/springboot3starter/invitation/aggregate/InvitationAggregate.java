package vn.eztek.springboot3starter.invitation.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.invitation.command.CheckMemberProjectCommand;
import vn.eztek.springboot3starter.invitation.command.CheckUserMatchInviteCommand;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.command.handler.CheckMemberProjectCommandHandler;
import vn.eztek.springboot3starter.invitation.command.handler.CheckUserMatchInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.command.handler.ConfirmInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.command.handler.InviteTalentCommandHandler;
import vn.eztek.springboot3starter.invitation.command.handler.ResponseInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.query.ListInvitationQuery;
import vn.eztek.springboot3starter.invitation.query.handler.ListInvitationQueryHandler;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class InvitationAggregate extends AggregateRoot<InvitationAggregate, InvitationAggregateId> {

  public InvitationAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new InvitationAggregateId());
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    behaviorBuilder.setCommandHandler(InviteTalentCommand.class,
        getCommandHandler(InviteTalentCommandHandler.class));
    behaviorBuilder.setCommandHandler(ResponseInviteCommand.class,
        getCommandHandler(ResponseInviteCommandHandler.class));
    behaviorBuilder.setCommandHandler(CheckUserMatchInviteCommand.class,
        getCommandHandler(CheckUserMatchInviteCommandHandler.class));
    behaviorBuilder.setCommandHandler(CheckMemberProjectCommand.class,
        getCommandHandler(CheckMemberProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(ConfirmInviteCommand.class,
        getCommandHandler(ConfirmInviteCommandHandler.class));

    behaviorBuilder.setQueryHandler(ListInvitationQuery.class,
        getQueryHandler(ListInvitationQueryHandler.class));

    return behaviorBuilder.build();
  }

  @Override
  public boolean sameIdentityAs(InvitationAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public InvitationAggregateId id() {
    return entityId;
  }
}
