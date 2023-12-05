package vn.eztek.springboot3starter.notification.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.notification.query.CountUnreadNotificationQuery;
import vn.eztek.springboot3starter.notification.query.validator.CountUnreadNotificationQueryValidator;
import vn.eztek.springboot3starter.notification.response.CountNotificationResponse;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;


@Component
@RequiredArgsConstructor
public class CountUnreadNotificationQueryHandler implements
        QueryHandler<CountUnreadNotificationQuery, CountNotificationResponse, NotificationAggregateId> {
  private final CountUnreadNotificationQueryValidator validator;
  private final NotificationRepository notificationRepository;

  @Override
  public CountNotificationResponse handle(CountUnreadNotificationQuery query, NotificationAggregateId entityId) {
    //validate
    var validate = validator.validate(query);

    //handle
    var user = validate.getUser();
    var notifications = notificationRepository.countByUserIdAndViewAtNull(
            user.getId());

    return CountNotificationResponse.countOf(notifications);
  }
}
