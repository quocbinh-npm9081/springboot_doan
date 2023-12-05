package vn.eztek.springboot3starter.common.redis.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;
import vn.eztek.springboot3starter.shared.socket.NotificationResponse;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MessageMapper {

  @Mappings({@Mapping(target = "metadata", source = "notification.metadata"),
      @Mapping(target = "notificationType", source = "notification.notificationType"),
      @Mapping(target = "readAt", source = "notification.readAt"),
      @Mapping(target = "lastModifiedDate", ignore = true),
      @Mapping(target = "lastModifiedBy", ignore = true),})
  public abstract NotificationResponse mapToNotificationResponse(Notification notification);

  public abstract Notification mapBeforeCreateNotification(SocketResponseType notificationType,
      JsonNode metadata, UUID userId);

}
