package vn.eztek.springboot3starter.auth.service;

import vn.eztek.springboot3starter.auth.command.event.AccountActivatedEvent;
import vn.eztek.springboot3starter.auth.command.event.AccountRestoreFinishedEvent;
import vn.eztek.springboot3starter.auth.command.event.AccountVerifiedEvent;
import vn.eztek.springboot3starter.auth.command.event.ActivateAccountFinishedEvent;
import vn.eztek.springboot3starter.auth.command.event.RegisteringUserCheckedEvent;
import vn.eztek.springboot3starter.auth.command.event.TokenRefreshEvent;
import vn.eztek.springboot3starter.auth.command.event.UserChangeEmailEvent;
import vn.eztek.springboot3starter.auth.command.event.UserChangeEmailFinishedEvent;
import vn.eztek.springboot3starter.auth.command.event.UserFinishSignUpEvent;
import vn.eztek.springboot3starter.auth.command.event.UserForgotPasswordEvent;
import vn.eztek.springboot3starter.auth.command.event.UserResendForgotPasswordEvent;
import vn.eztek.springboot3starter.auth.command.event.UserResetPasswordEvent;
import vn.eztek.springboot3starter.auth.command.event.UserRestoreAccountEvent;
import vn.eztek.springboot3starter.auth.command.event.UserSignedInEvent;

public interface AuthEventStoreService {

  void store(UserSignedInEvent event);

  void store(UserForgotPasswordEvent event);

  void store(UserResetPasswordEvent event);

  void store(UserResendForgotPasswordEvent event);

  void store(UserChangeEmailEvent event);

  void store(UserChangeEmailFinishedEvent event);

  void store(TokenRefreshEvent event);

  void store(UserFinishSignUpEvent event);

  void store(RegisteringUserCheckedEvent event);

  void store(UserRestoreAccountEvent event);

  void store(AccountRestoreFinishedEvent event);

  void store(AccountActivatedEvent event);

  void store(ActivateAccountFinishedEvent event);

  void store(AccountVerifiedEvent event);

}
