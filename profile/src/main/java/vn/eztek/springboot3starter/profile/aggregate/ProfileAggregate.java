package vn.eztek.springboot3starter.profile.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.profile.command.ChangePasswordCommand;
import vn.eztek.springboot3starter.profile.command.DeleteProfileCommand;
import vn.eztek.springboot3starter.profile.command.DeactivateProfileCommand;
import vn.eztek.springboot3starter.profile.command.RequestChangeEmailCommand;
import vn.eztek.springboot3starter.profile.command.UpdateProfileCommand;
import vn.eztek.springboot3starter.profile.command.handler.ChangePasswordCommandHandler;
import vn.eztek.springboot3starter.profile.command.handler.DeleteProfileCommandHandler;
import vn.eztek.springboot3starter.profile.command.handler.DeactivateProfileCommandHandler;
import vn.eztek.springboot3starter.profile.command.handler.RequestChangeEmailCommandHandler;
import vn.eztek.springboot3starter.profile.command.handler.UpdateProfileCommandHandler;
import vn.eztek.springboot3starter.profile.query.GetMyProfileQuery;
import vn.eztek.springboot3starter.profile.query.handler.GetMyProfileQueryHandler;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class ProfileAggregate extends AggregateRoot<ProfileAggregate, ProfileAggregateId> {

  public ProfileAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new ProfileAggregateId());
  }

  public ProfileAggregate(ApplicationContext applicationContext,
      ProfileAggregateId profileAggregateId) {
    super(applicationContext, profileAggregateId);
  }

  @Override
  public boolean sameIdentityAs(ProfileAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public ProfileAggregateId id() {
    return entityId;
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    // command
    behaviorBuilder.setCommandHandler(ChangePasswordCommand.class,
        getCommandHandler(ChangePasswordCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateProfileCommand.class,
        getCommandHandler(UpdateProfileCommandHandler.class));
    behaviorBuilder.setCommandHandler(RequestChangeEmailCommand.class,
        getCommandHandler(RequestChangeEmailCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteProfileCommand.class,
        getCommandHandler(DeleteProfileCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeactivateProfileCommand.class,
        getCommandHandler(DeactivateProfileCommandHandler.class));

    // query
    behaviorBuilder.setQueryHandler(GetMyProfileQuery.class,
        getQueryHandler(GetMyProfileQueryHandler.class));
    return behaviorBuilder.build();
  }

}
