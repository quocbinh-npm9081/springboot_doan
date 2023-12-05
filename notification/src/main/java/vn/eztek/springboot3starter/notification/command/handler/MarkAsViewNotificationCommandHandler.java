package vn.eztek.springboot3starter.notification.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.notification.command.MarkAsViewNotificationCommand;
import vn.eztek.springboot3starter.notification.command.event.MarkAsViewNotificationEvent;
import vn.eztek.springboot3starter.notification.command.validator.MarkAsViewNotificationCommandValidator;
import vn.eztek.springboot3starter.notification.service.NotificationEventStoreService;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarkAsViewNotificationCommandHandler implements
        CommandHandler<MarkAsViewNotificationCommand, EmptyCommandResult, NotificationAggregateId> {

  private final NotificationEventStoreService eventStoreService;
  private final NotificationRepository notificationRepository;
  private final MarkAsViewNotificationCommandValidator validator;

  @Override
  public EmptyCommandResult handle(MarkAsViewNotificationCommand command,
                                   NotificationAggregateId entityId) {
    //validate
    var validate = validator.validate(command);

    //handle
    var user = validate.getUser();
    List<Notification> notifications = notificationRepository.findAllByUserIdAndViewAtNull(
            user.getId());
    for (var notification : notifications) {
      notification.setViewAt(ZonedDateTime.now());
    }
    notificationRepository.saveAll(notifications);

    //event store
    var event = MarkAsViewNotificationEvent.eventOf(entityId, user.getId().toString());
    eventStoreService.store(event);

    return EmptyCommandResult.empty();

  }
}
