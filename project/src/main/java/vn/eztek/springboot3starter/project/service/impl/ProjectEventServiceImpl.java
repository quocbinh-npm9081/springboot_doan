package vn.eztek.springboot3starter.project.service.impl;

import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.project.command.event.MembersAddedToProjectEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectCreatedEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectDeletedEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectRestoredEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectUpdatedEvent;
import vn.eztek.springboot3starter.project.command.event.SharedLinkCreatedEvent;
import vn.eztek.springboot3starter.project.command.event.SharedLinkDeletedEvent;
import vn.eztek.springboot3starter.project.command.event.StagesUpdatedEvent;
import vn.eztek.springboot3starter.project.command.event.StatusMemberUpdatedInProjectEvent;
import vn.eztek.springboot3starter.project.service.ProjectEventService;

@Service
public class ProjectEventServiceImpl implements ProjectEventService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  public ProjectEventServiceImpl(EventStoreRepository eventStoreRepository, JsonMapper jsonMapper) {
    this.eventStoreRepository = eventStoreRepository;
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void store(ProjectCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ProjectUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(MembersAddedToProjectEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.INVITE_MEMBERS);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(StagesUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_STAGE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(StatusMemberUpdatedInProjectEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_STATUS_MEMBER_IN_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ProjectDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(ProjectRestoredEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.RESTORE_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(SharedLinkCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_LINK_SHARE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(SharedLinkDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_LINK_SHARE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
