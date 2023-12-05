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
import vn.eztek.springboot3starter.task.command.checklistitem.CreateCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.CheckListItemCreatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.CreateCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class CreateCheckListItemCommandHandler implements
    CommandHandler<CreateCheckListItemCommand, CheckListItemResponse, TaskAggregateId> {

  private final CreateCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListItemRepository checkListItemRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListItemResponse handle(CreateCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = taskMapper.mapToCheckListItemBeforeCreate(command.getContent(),
        validated.getCheckList());

    checkListItem = checkListItemRepository.save(checkListItem);

    var event = CheckListItemCreatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), checkListItem.getId().toString(),
        command.getContent());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_ADDED, gson.toJson(
                CheckListItemSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), checkListItem.getId().toString(),
                    checkListItem.getContent())))));

    // returning
    return taskMapper.mapToCheckListItemResponse(checkListItem);
  }

}
