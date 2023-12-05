package vn.eztek.springboot3starter.invitation.service.impl;

import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.invitation.command.event.InviteConfirmEvent;
import vn.eztek.springboot3starter.invitation.command.event.InviteResponsedEvent;
import vn.eztek.springboot3starter.invitation.command.event.MemberProjectCheckedEvent;
import vn.eztek.springboot3starter.invitation.command.event.TalentInviteEvent;
import vn.eztek.springboot3starter.invitation.command.event.UserMatchInviteCheckedEvent;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;

@Service
public class InvitationEventServiceImpl implements InvitationEventService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  public InvitationEventServiceImpl(EventStoreRepository eventStoreRepository,
      JsonMapper jsonMapper) {
    this.eventStoreRepository = eventStoreRepository;
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void store(TalentInviteEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.INVITE_TALENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(InviteResponsedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.RESPONSE_INVITE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(UserMatchInviteCheckedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHECK_USER_MATCH_INVITE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(InviteConfirmEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CONFIRM_INVITE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(MemberProjectCheckedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CHECK_MEMBER_PROJECT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
