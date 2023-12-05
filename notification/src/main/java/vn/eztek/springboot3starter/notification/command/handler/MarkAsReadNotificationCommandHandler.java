package vn.eztek.springboot3starter.notification.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.notification.command.MarkAsReadNotificationCommand;
import vn.eztek.springboot3starter.notification.command.event.MarkAsReadNotificationEvent;
import vn.eztek.springboot3starter.notification.command.validator.MarkAsReadNotificationCommandValidator;
import vn.eztek.springboot3starter.notification.service.NotificationEventStoreService;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class MarkAsReadNotificationCommandHandler implements
        CommandHandler<MarkAsReadNotificationCommand, EmptyCommandResult, NotificationAggregateId> {

  private final MarkAsReadNotificationCommandValidator validator;
  private final NotificationEventStoreService eventStoreService;
  private final NotificationRepository notificationRepository;

  @Override
  public EmptyCommandResult handle(MarkAsReadNotificationCommand command,
                                   NotificationAggregateId entityId) {
    //validate
    var validate = validator.validate(command);

    //handle
    var user = validate.getUser();
    var notifications = validate.getNotification();
    for (var notification : notifications) {
      notification.setReadAt(ZonedDateTime.now());
    }
    notificationRepository.saveAll(notifications);

    //event store
    var event = MarkAsReadNotificationEvent.eventOf(entityId, user.getId().toString());
    eventStoreService.store(event);

    return EmptyCommandResult.empty();
  }
}
