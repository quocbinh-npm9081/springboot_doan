package vn.eztek.springboot3starter.project.service;

import vn.eztek.springboot3starter.project.command.event.MembersAddedToProjectEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectCreatedEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectDeletedEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectRestoredEvent;
import vn.eztek.springboot3starter.project.command.event.ProjectUpdatedEvent;
import vn.eztek.springboot3starter.project.command.event.SharedLinkCreatedEvent;
import vn.eztek.springboot3starter.project.command.event.SharedLinkDeletedEvent;
import vn.eztek.springboot3starter.project.command.event.StagesUpdatedEvent;
import vn.eztek.springboot3starter.project.command.event.StatusMemberUpdatedInProjectEvent;

public interface ProjectEventService {

  void store(ProjectCreatedEvent event);

  void store(ProjectUpdatedEvent event);

  void store(MembersAddedToProjectEvent event);

  void store(StagesUpdatedEvent event);

  void store(StatusMemberUpdatedInProjectEvent event);

  void store(ProjectDeletedEvent event);

  void store(ProjectRestoredEvent event);

  void store(SharedLinkCreatedEvent event);

  void store(SharedLinkDeletedEvent event);

}
