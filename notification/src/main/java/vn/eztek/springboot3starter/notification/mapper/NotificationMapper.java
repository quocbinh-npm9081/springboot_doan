package vn.eztek.springboot3starter.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;
import vn.eztek.springboot3starter.notification.response.NotificationResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public abstract class NotificationMapper {

  public abstract NotificationResponse mapToNotificationResponse(Notification notification);
}
