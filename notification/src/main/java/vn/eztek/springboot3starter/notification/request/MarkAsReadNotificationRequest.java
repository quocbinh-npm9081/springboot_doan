package vn.eztek.springboot3starter.notification.request;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkAsReadNotificationRequest {

  @Size(min = 1, message = "required-at-least-one-item")
  List<UUID> notificationIds;

}
