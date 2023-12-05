package vn.eztek.springboot3starter.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;

@Service
@RequiredArgsConstructor
public class AuthEventStoreServiceImpl implements AuthEventStoreService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  @Override
  @Transactional
  public void store(UserSignedInEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.SIGN_IN);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserForgotPasswordEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.FORGOT_PASSWORD);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserResetPasswordEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.RESET_PASSWORD);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserResendForgotPasswordEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.RESEND_FORGOT_PASSWORD);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserChangeEmailEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHANGE_EMAIL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }


  @Override
  @Transactional
  public void store(UserChangeEmailFinishedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHANGE_EMAIL_FINISHED);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(TokenRefreshEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.REFRESH_TOKEN);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserFinishSignUpEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CONFIRM_REGISTER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(RegisteringUserCheckedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHECK_REGISTERING_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserRestoreAccountEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.RESTORE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(AccountRestoreFinishedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.FINISH_RESTORE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(AccountActivatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.ACTIVE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ActivateAccountFinishedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.FINISH_ACTIVE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(AccountVerifiedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.VERIFY_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
