package vn.eztek.springboot3starter.profile.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.profile.command.event.EmailRequestChangedEvent;
import vn.eztek.springboot3starter.profile.command.event.PasswordChangedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeletedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeactivatedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileUpdatedEvent;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;

@Service
@RequiredArgsConstructor
public class ProfileEventStoreServiceImpl implements ProfileEventStoreService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  @Override
  public void store(PasswordChangedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHANGE_PASSWORD);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(ProfileUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(EmailRequestChangedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.REQUEST_CHANGE_EMAIL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ProfileDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ProfileDeactivatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.SAFE_DISABLE_ACCOUNT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
