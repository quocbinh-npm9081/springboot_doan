package vn.eztek.springboot3starter.user.service;

import vn.eztek.springboot3starter.user.command.event.UserCreatedEvent;
import vn.eztek.springboot3starter.user.command.event.UserDeletedEvent;
import vn.eztek.springboot3starter.user.command.event.UserStatusUpdatedEvent;
import vn.eztek.springboot3starter.user.command.event.UserUpdatedEvent;

public interface UserEventStoreService {

  void store(UserCreatedEvent event);

  void store(UserUpdatedEvent event);

  void store(UserDeletedEvent event);

  void store(UserStatusUpdatedEvent event);
}
