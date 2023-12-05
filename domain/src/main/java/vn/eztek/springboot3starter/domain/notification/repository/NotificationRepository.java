package vn.eztek.springboot3starter.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.notification.entity.Notification;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  Page<Notification> findAllByUserId(UUID userId, Pageable pageable);

  List<Notification> findAllByUserIdAndIdIn(UUID userId, Set<UUID> notificationIds);

  List<Notification> findAllByUserIdAndViewAtNull(UUID userId);

  Long countByUserIdAndViewAtNull(UUID userId);

}
