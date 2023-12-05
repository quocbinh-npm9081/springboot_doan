package vn.eztek.springboot3starter.notification.service;


import vn.eztek.springboot3starter.notification.command.event.MarkAsReadNotificationEvent;
import vn.eztek.springboot3starter.notification.command.event.MarkAsViewNotificationEvent;

public interface NotificationEventStoreService {

  void store(MarkAsReadNotificationEvent event);

  void store(MarkAsViewNotificationEvent event);


}
