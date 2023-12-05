package vn.eztek.springboot3starter.invitation.service;

import vn.eztek.springboot3starter.invitation.command.event.InviteConfirmEvent;
import vn.eztek.springboot3starter.invitation.command.event.InviteResponsedEvent;
import vn.eztek.springboot3starter.invitation.command.event.MemberProjectCheckedEvent;
import vn.eztek.springboot3starter.invitation.command.event.TalentInviteEvent;
import vn.eztek.springboot3starter.invitation.command.event.UserMatchInviteCheckedEvent;

public interface InvitationEventService {

  void store(TalentInviteEvent event);

  void store(InviteResponsedEvent event);

  void store(UserMatchInviteCheckedEvent event);

  void store(InviteConfirmEvent event);

  void store(MemberProjectCheckedEvent event);

}
