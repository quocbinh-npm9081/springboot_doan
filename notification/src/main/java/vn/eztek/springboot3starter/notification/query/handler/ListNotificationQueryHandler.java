package vn.eztek.springboot3starter.notification.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.notification.mapper.NotificationMapper;
import vn.eztek.springboot3starter.notification.query.ListNotificationQuery;
import vn.eztek.springboot3starter.notification.query.validator.ListNotificationQueryValidator;
import vn.eztek.springboot3starter.notification.response.NotificationResponse;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.PageResponse;

@Component
@RequiredArgsConstructor
public class ListNotificationQueryHandler implements
    QueryHandler<ListNotificationQuery, PageResponse<NotificationResponse>, NotificationAggregateId> {

  private final ListNotificationQueryValidator validator;
  private final NotificationMapper notificationMapper;
  private final NotificationRepository notificationRepository;

  @Override
  public PageResponse<NotificationResponse> handle(ListNotificationQuery query,
      NotificationAggregateId entityId) {
    //validating
    var validated = validator.validate(query);

    //handling
    var notifications = notificationRepository.findAllByUserId(validated.getUser().getId(),
        query.getPageable());
    var result = notifications.stream().map(notificationMapper::mapToNotificationResponse)
        .toList();
    //resulting
    return new PageResponse<>(result, query.getPageable(), notifications.getTotalElements());
  }
}
