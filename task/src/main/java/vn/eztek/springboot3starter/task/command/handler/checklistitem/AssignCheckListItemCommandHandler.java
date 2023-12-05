package vn.eztek.springboot3starter.task.command.handler.checklistitem;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.NotificationSocketMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.checkListItem.repository.CheckListItemRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.AssignCheckListItemSocketData;
import vn.eztek.springboot3starter.task.command.checklistitem.AssignCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.AssignListItemCreatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.AssignCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class AssignCheckListItemCommandHandler implements
    CommandHandler<AssignCheckListItemCommand, CheckListItemResponse, TaskAggregateId> {

  private final AssignCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListItemRepository checkListItemRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListItemResponse handle(AssignCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = validated.getCheckListItem();
    checkListItem.setAssignee(validated.getAssignee());
    checkListItem = checkListItemRepository.save(checkListItem);

    var event = AssignListItemCreatedEvent.eventOf(taskAggregateId, command.getTaskId().toString(),
        validated.getLoggedInUser().getId().toString(), command.getUserId().toString());

    eventStoreService.store(event);

    // send notification
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var taskId = jsonNodeFactory.textNode(command.getTaskId().toString());
    var assignorId = jsonNodeFactory.textNode(validated.getLoggedInUser().getId().toString());
    var checkListId = jsonNodeFactory.textNode(command.getCheckListId().toString());
    var checkListItemId = jsonNodeFactory.textNode(command.getId().toString());
    var metadata = jsonNodeFactory.objectNode();
    metadata.set("taskId", taskId);
    metadata.set("assignorId", assignorId);
    metadata.set("checkListId", checkListId);
    metadata.set("checkListItemId", checkListItemId);
    var userId = command.getUserId();

    redisMessagePublisher.publish(
        NotificationSocketMessage.create(SocketResponseType.CHECK_LIST_ITEM_ASSIGNED, metadata,
            SocketResponseType.CHECK_LIST_ITEM_ASSIGNED, userId));

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_ASSIGNED, gson.toJson(
                AssignCheckListItemSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), command.getId().toString(),
                    command.getUserId().toString())))));

    // returning
    return taskMapper.mapToCheckListItemResponse(checkListItem);
  }

}
