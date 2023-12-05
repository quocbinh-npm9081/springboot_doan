package vn.eztek.springboot3starter.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.auth.aggregate.AuthAggregate;
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
import vn.eztek.springboot3starter.auth.request.ActiveAccountRequest;
import vn.eztek.springboot3starter.auth.request.ChangeEmailRequest;
import vn.eztek.springboot3starter.auth.request.CheckUserRequest;
import vn.eztek.springboot3starter.auth.request.ConfirmRegisterRequest;
import vn.eztek.springboot3starter.auth.request.FinishActiveAccountRequest;
import vn.eztek.springboot3starter.auth.request.FinishChangeEmailRequest;
import vn.eztek.springboot3starter.auth.request.FinishRestoreAccountRequest;
import vn.eztek.springboot3starter.auth.request.ForgotPasswordRequest;
import vn.eztek.springboot3starter.auth.request.RefreshTokenRequest;
import vn.eztek.springboot3starter.auth.request.ResendForgotPasswordRequest;
import vn.eztek.springboot3starter.auth.request.ResetPasswordRequest;
import vn.eztek.springboot3starter.auth.request.RestoreAccountRequest;
import vn.eztek.springboot3starter.auth.request.SignInRequest;
import vn.eztek.springboot3starter.auth.response.CheckUserResponse;
import vn.eztek.springboot3starter.auth.response.JwtResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final ApplicationContext applicationContext;

  @PostMapping("/sign-in")
  public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody SignInRequest request) {
    var command = SignInCommand.commandOf(request.getUsername(), request.getPassword());

    var aggregate = new AuthAggregate(applicationContext);
    JwtResponse result = aggregate.handle(command);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<JwtResponse> refreshAuthenticationToken(
      @Valid @RequestBody RefreshTokenRequest request) {

    var command = RefreshTokenCommand.commandOf(request.getRefreshToken());

    var aggregate = new AuthAggregate(applicationContext);
    JwtResponse result = aggregate.handle(command);

    return ResponseEntity.ok(result);

  }

  @PostMapping("/confirm-register")
  public ResponseEntity<Void> confirmRegister(@Valid @RequestBody ConfirmRegisterRequest request) {
    var command = FinishSignUpCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();

  }

  @PostMapping("/check-registering-user")
  public ResponseEntity<CheckUserResponse> checkRegisteringUser(
      @Valid @RequestBody CheckUserRequest request) {
    var command = CheckRegisteringUserCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    CheckUserResponse check = aggregate.handle(command);

    return ResponseEntity.ok(check);

  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    var command = ForgotPasswordCommand.commandOf(request.getEmail());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();

  }

  @PostMapping("/forgot-password/resend")
  public ResponseEntity<Void> resendForgotPassword(
      @Valid @RequestBody ResendForgotPasswordRequest request) {
    var command = ResendForgotPasswordCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();

  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    var command = ResetPasswordCommand.commandOf(request.getPassword(), request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/change-email")
  public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
    var command = ChangeEmailCommand.commandOf(request.getUsername(), request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/finish-change-email")
  public ResponseEntity<Void> finishChangeEmail(
      @Valid @RequestBody FinishChangeEmailRequest request) {
    var command = FinishChangeEmailCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/restore-account")
  public ResponseEntity<Void> requestRestoreAccount(
      @Valid @RequestBody RestoreAccountRequest request) {
    var command = RestoreAccountCommand.commandOf(request.getEmail());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/finish-restore-account")
  public ResponseEntity<Void> finishRestoreAccount(
      @Valid @RequestBody FinishRestoreAccountRequest request) {
    var command = FinishRestoreAccountCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/verify-account")
  public ResponseEntity<Void> verifyAccount(
      @Valid @RequestBody FinishActiveAccountRequest request) {
    var command = VerifyAccountCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/activate-account")
  public ResponseEntity<Void> activateAccount(@Valid @RequestBody ActiveAccountRequest request) {
    var command = ActivateAccountCommand.commandOf(request.getEmail());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/finish-activate-account")
  public ResponseEntity<Void> finishActivateAccount(
      @Valid @RequestBody FinishActiveAccountRequest request) {
    var command = FinishActivateAccountCommand.commandOf(request.getKey());

    var aggregate = new AuthAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

}
