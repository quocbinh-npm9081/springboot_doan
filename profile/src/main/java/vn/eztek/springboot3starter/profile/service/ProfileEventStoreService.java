package vn.eztek.springboot3starter.profile.service;

import vn.eztek.springboot3starter.profile.command.event.EmailRequestChangedEvent;
import vn.eztek.springboot3starter.profile.command.event.PasswordChangedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeletedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeactivatedEvent;
import vn.eztek.springboot3starter.profile.command.event.ProfileUpdatedEvent;

public interface ProfileEventStoreService {

  void store(PasswordChangedEvent event);

  void store(ProfileUpdatedEvent event);

  void store(EmailRequestChangedEvent event);

  void store(ProfileDeletedEvent event);

  void store(ProfileDeactivatedEvent event);

}
