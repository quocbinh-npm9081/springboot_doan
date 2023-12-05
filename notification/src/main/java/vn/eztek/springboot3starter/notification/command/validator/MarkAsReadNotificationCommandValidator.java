package vn.eztek.springboot3starter.notification.command.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.notification.repository.NotificationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.notification.command.MarkAsReadNotificationCommand;
import vn.eztek.springboot3starter.notification.command.validated.MarkAsReadNotificationCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MarkAsReadNotificationCommandValidator extends
    CommandValidation<MarkAsReadNotificationCommand, MarkAsReadNotificationCommandValidated> {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;

  @Override
  public MarkAsReadNotificationCommandValidated validate(MarkAsReadNotificationCommand command) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(authentication.getName())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }
    Set<UUID> notificationIds = new HashSet<>(command.getNotificationIds());
    var notifications = notificationRepository.findAllByUserIdAndIdIn(user.getId(),
        notificationIds);

    if (notifications.size() != notificationIds.size()) {
      throw new BadRequestException("invalid-notification-list");
    }

    for (var notification : notifications) {
      if (notification.getReadAt() != null) {
        throw new BadRequestException("notification-is-read");
      }
    }
    return MarkAsReadNotificationCommandValidated.validatedOf(notifications, user);
  }
}
