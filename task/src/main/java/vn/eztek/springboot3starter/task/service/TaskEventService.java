package vn.eztek.springboot3starter.task.service;

import vn.eztek.springboot3starter.task.command.event.*;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentDeletedEvent;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentUpdatedEvent;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentUploadedEvent;
import vn.eztek.springboot3starter.task.command.event.checklist.CheckListCreatedEvent;
import vn.eztek.springboot3starter.task.command.event.checklist.CheckListDeletedEvent;
import vn.eztek.springboot3starter.task.command.event.checklist.CheckListUpdatedEvent;
import vn.eztek.springboot3starter.task.command.event.checklistitem.*;
import vn.eztek.springboot3starter.task.command.event.comment.CommentDeletedEvent;
import vn.eztek.springboot3starter.task.command.event.comment.CommentPostedEvent;
import vn.eztek.springboot3starter.task.command.event.comment.CommentUpdatedEvent;
import vn.eztek.springboot3starter.task.command.event.label.*;

public interface TaskEventService {

  void store(TaskCreatedEvent event);

  void store(TaskUpdatedEvent event);

  void store(TaskAssignedEvent event);

  void store(TaskBatchUpdatedEvent event);

  void store(TaskDeletedEvent event);

  void store(AttachmentUploadedEvent event);

  void store(AttachmentDeletedEvent event);

  void store(CommentPostedEvent event);

  void store(CommentUpdatedEvent event);

  void store(CommentDeletedEvent event);

  void store(TaskUnassignedEvent event);

  void store(AttachmentUpdatedEvent event);

  void store(CheckListCreatedEvent event);

  void store(CheckListUpdatedEvent event);

  void store(CheckListDeletedEvent event);

  void store(CheckListItemCreatedEvent event);

  void store(CheckListItemUpdatedEvent event);

  void store(CheckListItemDeletedEvent event);

  void store(AssignListItemCreatedEvent event);

  void store(UnAssignListItemCreatedEvent event);

  void store(CheckListItemDueDateUpdatedEvent event);

  void store(CheckListItemStatusUpdatedEvent event);

  void store(LabelCreatedEvent event);

  void store(LabelUpdatedEvent event);

  void store(LabelDeletedEvent event);

  void store(LabelAssignedInTaskEvent event);

  void store(LabelUnAssignedInTaskEvent event);

  void store(LabelRemovedColorEvent event);

}
