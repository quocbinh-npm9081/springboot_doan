package vn.eztek.springboot3starter.task.command.handler.checklistitem;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.checkListItem.repository.CheckListItemRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.UnAssignCheckListItemSocketData;
import vn.eztek.springboot3starter.task.command.checklistitem.UnAssignCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.UnAssignListItemCreatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.UnAssignCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UnAssignCheckListItemCommandHandler implements
    CommandHandler<UnAssignCheckListItemCommand, CheckListItemResponse, TaskAggregateId> {

  private final UnAssignCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListItemRepository checkListItemRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListItemResponse handle(UnAssignCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = validated.getCheckListItem();
    checkListItem.setAssignee(null);
    checkListItem = checkListItemRepository.save(checkListItem);

    var event = UnAssignListItemCreatedEvent.eventOf(taskAggregateId,
        command.getTaskId().toString(), validated.getLoggedInUser().getId().toString());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_UNASSIGNED, gson.toJson(
                UnAssignCheckListItemSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), command.getId().toString())))));

    // returning
    return taskMapper.mapToCheckListItemResponse(checkListItem);
  }

}
