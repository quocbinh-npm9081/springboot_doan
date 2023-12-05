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
import vn.eztek.springboot3starter.shared.socket.response.CheckListItemSocketData;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.CheckListItemUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.UpdateCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateCheckListItemCommandHandler implements
    CommandHandler<UpdateCheckListItemCommand, CheckListItemResponse, TaskAggregateId> {

  private final UpdateCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListItemRepository checkListItemRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListItemResponse handle(UpdateCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = taskMapper.mapToCheckListItemBeforeUpdate(validated.getCheckListItem(),
        command.getContent());

    checkListItem = checkListItemRepository.save(checkListItem);

    var event = CheckListItemUpdatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), checkListItem.getId().toString(),
        checkListItem.getContent());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_UPDATED, gson.toJson(
                CheckListItemSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), checkListItem.getId().toString(),
                    checkListItem.getContent())))));

    // returning
    return taskMapper.mapToCheckListItemResponse(checkListItem);
  }

}
