package vn.eztek.springboot3starter.notification.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.notification.command.event.MarkAsReadNotificationEvent;
import vn.eztek.springboot3starter.notification.command.event.MarkAsViewNotificationEvent;
import vn.eztek.springboot3starter.notification.service.NotificationEventStoreService;

@Service
@RequiredArgsConstructor
public class NotificationEventStoreServiceImpl implements NotificationEventStoreService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  @Override
  @Transactional
  public void store(MarkAsReadNotificationEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.MARK_AS_READ_NOTIFICATION);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(MarkAsViewNotificationEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.MARK_AS_VIEWED_NOTIFICATION);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
