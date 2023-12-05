package vn.eztek.springboot3starter.task.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.mapper.JsonMapper;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
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
import vn.eztek.springboot3starter.task.service.TaskEventService;

@Service
public class TaskEventServiceImpl implements TaskEventService {

  private final EventStoreRepository eventStoreRepository;
  private final JsonMapper jsonMapper;

  public TaskEventServiceImpl(EventStoreRepository eventStoreRepository,
                              JsonMapper jsonMapper) {
    this.eventStoreRepository = eventStoreRepository;
    this.jsonMapper = jsonMapper;
  }

  @Override
  @Transactional
  public void store(TaskCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.PATCH_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(TaskUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(TaskAssignedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.ASSIGN_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(TaskDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(TaskBatchUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.BATCH_UPDATE_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(AttachmentUploadedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.ADD_ATTACHMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  @Transactional
  public void store(AttachmentDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_ATTACHMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CommentPostedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.POST_COMMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CommentUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_COMMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CommentDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_COMMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(TaskUnassignedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UNASSIGN_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(AttachmentUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_ATTACHMENT);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_CHECK_LIST);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_CHECK_LIST);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_CHECK_LIST);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListItemCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListItemUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListItemDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(AssignListItemCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.ASSIGN_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(UnAssignListItemCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UNASSIGN_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListItemDueDateUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_DUE_DATE_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(CheckListItemStatusUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_STATUS_CHECK_LIST_ITEM);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelCreatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.CREATE_LABEL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelUpdatedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.UPDATE_LABEL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelDeletedEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.DELETE_LABEL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelAssignedInTaskEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.ADD_LABEL_IN_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelUnAssignedInTaskEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.REMOVE_LABEL_IN_TASK);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

  @Override
  public void store(LabelRemovedColorEvent event) {
    var eventDataAsJson = jsonMapper.write(event);
    var eventStore = new EventStore();
    eventStore.setEventId(event.getId().id);
    eventStore.setUserId(event.getUserId());
    eventStore.setAction(EventAction.REMOVE_COLOR_IN_LABEL);
    eventStore.setEventData(eventDataAsJson);
    eventStoreRepository.save(eventStore);
  }

}
