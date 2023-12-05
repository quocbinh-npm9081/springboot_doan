package vn.eztek.springboot3starter.publicAccess.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationMoveOnEvent;
import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationViaLinkEvent;
import vn.eztek.springboot3starter.publicAccess.service.PublicAccessEventService;

@Service
public class PublicAccessEventServiceImpl implements PublicAccessEventService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  public PublicAccessEventServiceImpl(EventStoreRepository eventStoreRepository,
      JsonMapper jsonMapper) {
    this.eventStoreRepository = eventStoreRepository;
    this.jsonMapper = jsonMapper;
  }

  @Override
  @Transactional
  public void store(UserInvitationMoveOnEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.REGISTERING_USER);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(UserInvitationViaLinkEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_ACCOUNT_VIA_LINK_INVITE);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }


}
