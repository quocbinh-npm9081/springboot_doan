package vn.eztek.springboot3starter.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.user.command.event.UserCreatedEvent;
import vn.eztek.springboot3starter.user.command.event.UserDeletedEvent;
import vn.eztek.springboot3starter.user.command.event.UserStatusUpdatedEvent;
import vn.eztek.springboot3starter.user.command.event.UserUpdatedEvent;
import vn.eztek.springboot3starter.user.service.UserEventStoreService;

@Service
@RequiredArgsConstructor
public class UserEventStoreServiceImpl implements UserEventStoreService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  @Override
  @Transactional
  public void store(UserCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(UserDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(UserStatusUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DEACTIVATE_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
