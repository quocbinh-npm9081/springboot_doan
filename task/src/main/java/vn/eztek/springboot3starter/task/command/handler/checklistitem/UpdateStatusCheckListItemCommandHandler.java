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
import vn.eztek.springboot3starter.shared.socket.response.CheckListItemStatusDateSocketData;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateStatusCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.CheckListItemStatusUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.UpdateStatusCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateStatusCheckListItemCommandHandler implements
    CommandHandler<UpdateStatusCheckListItemCommand, CheckListItemResponse, TaskAggregateId> {

  private final UpdateStatusCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListItemRepository checkListItemRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListItemResponse handle(UpdateStatusCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = validated.getCheckListItem();
    checkListItem.setIsDone(command.getStatus());
    checkListItem = checkListItemRepository.save(checkListItem);

    var event = CheckListItemStatusUpdatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), checkListItem.getId().toString(),
        command.getStatus());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_STATUS_UPDATED, gson.toJson(
                CheckListItemStatusDateSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), checkListItem.getId().toString(),
                    command.getStatus())))));

    // returning
    return taskMapper.mapToCheckListItemResponse(checkListItem);
  }

}
