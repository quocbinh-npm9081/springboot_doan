package vn.eztek.springboot3starter.publicAccess.service;

import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationMoveOnEvent;
import vn.eztek.springboot3starter.publicAccess.command.event.UserInvitationViaLinkEvent;

public interface PublicAccessEventService {

  void store(UserInvitationMoveOnEvent event);

  void store(UserInvitationViaLinkEvent event);
}
