package vn.eztek.springboot3starter.auth.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.auth.command.ActivateAccountCommand;
import vn.eztek.springboot3starter.auth.command.ChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.CheckRegisteringUserCommand;
import vn.eztek.springboot3starter.auth.command.FinishActivateAccountCommand;
import vn.eztek.springboot3starter.auth.command.FinishChangeEmailCommand;
import vn.eztek.springboot3starter.auth.command.FinishRestoreAccountCommand;
import vn.eztek.springboot3starter.auth.command.FinishSignUpCommand;
import vn.eztek.springboot3starter.auth.command.ForgotPasswordCommand;
import vn.eztek.springboot3starter.auth.command.RefreshTokenCommand;
import vn.eztek.springboot3starter.auth.command.ResendForgotPasswordCommand;
import vn.eztek.springboot3starter.auth.command.ResetPasswordCommand;
import vn.eztek.springboot3starter.auth.command.RestoreAccountCommand;
import vn.eztek.springboot3starter.auth.command.SignInCommand;
import vn.eztek.springboot3starter.auth.command.VerifyAccountCommand;
import vn.eztek.springboot3starter.auth.command.handler.ActivateAccountCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.ChangeEmailCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.CheckRegisteringUserCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.FinishActivateAccountCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.FinishChangeEmailCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.FinishRestoreAccountCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.FinishSignUpCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.ForgotPasswordCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.RefreshTokenCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.ResendForgotPasswordCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.ResetPasswordCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.RestoreAccountCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.SignInCommandHandler;
import vn.eztek.springboot3starter.auth.command.handler.VerifyAccountCommandHandler;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class AuthAggregate
    extends AggregateRoot<AuthAggregate, AuthAggregateId> {

  public AuthAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new AuthAggregateId());
  }

  @Override
  public boolean sameIdentityAs(AuthAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public AuthAggregateId id() {
    return entityId;
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    // get credentials
    behaviorBuilder.setCommandHandler(SignInCommand.class,
        getCommandHandler(SignInCommandHandler.class));
    behaviorBuilder.setCommandHandler(RefreshTokenCommand.class,
        getCommandHandler(RefreshTokenCommandHandler.class));

    // forgot password
    behaviorBuilder.setCommandHandler(ForgotPasswordCommand.class,
        getCommandHandler(ForgotPasswordCommandHandler.class));
    behaviorBuilder.setCommandHandler(ResetPasswordCommand.class,
        getCommandHandler(ResetPasswordCommandHandler.class));
    behaviorBuilder.setCommandHandler(ResendForgotPasswordCommand.class,
        getCommandHandler(ResendForgotPasswordCommandHandler.class));

    behaviorBuilder.setCommandHandler(FinishSignUpCommand.class,
        getCommandHandler(FinishSignUpCommandHandler.class));

    // check user registering
    behaviorBuilder.setCommandHandler(CheckRegisteringUserCommand.class,
        getCommandHandler(CheckRegisteringUserCommandHandler.class));

    // change email
    behaviorBuilder.setCommandHandler(ChangeEmailCommand.class,
        getCommandHandler(ChangeEmailCommandHandler.class));
    behaviorBuilder.setCommandHandler(FinishChangeEmailCommand.class,
        getCommandHandler(FinishChangeEmailCommandHandler.class));

    // restore account
    behaviorBuilder.setCommandHandler(RestoreAccountCommand.class,
        getCommandHandler(RestoreAccountCommandHandler.class));
    behaviorBuilder.setCommandHandler(FinishRestoreAccountCommand.class,
        getCommandHandler(FinishRestoreAccountCommandHandler.class));

    // active account
    behaviorBuilder.setCommandHandler(ActivateAccountCommand.class,
        getCommandHandler(ActivateAccountCommandHandler.class));
    behaviorBuilder.setCommandHandler(FinishActivateAccountCommand.class,
        getCommandHandler(FinishActivateAccountCommandHandler.class));

    // verify account
    behaviorBuilder.setCommandHandler(VerifyAccountCommand.class,
        getCommandHandler(VerifyAccountCommandHandler.class));

    return behaviorBuilder.build();

  }
}
