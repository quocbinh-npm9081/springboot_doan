package vn.eztek.springboot3starter.task.command.handler.checklistitem;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.checkListItem.repository.CheckListItemRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteCheckListItemSocketData;
import vn.eztek.springboot3starter.task.command.checklistitem.DeleteCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.event.checklistitem.CheckListItemDeletedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklistitem.DeleteCheckListItemCommandValidator;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteCheckListItemCommandHandler implements
    CommandHandler<DeleteCheckListItemCommand, EmptyCommandResult, TaskAggregateId> {

  private final DeleteCheckListItemCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final CheckListItemRepository checkListItemRepository;

  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public EmptyCommandResult handle(DeleteCheckListItemCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkListItem = validated.getCheckListItem();
    checkListItemRepository.delete(checkListItem);

    var event = CheckListItemDeletedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(),
        validated.getCheckListItem().getId().toString());
    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ITEM_DELETED, gson.toJson(
                DeleteCheckListItemSocketData.create(command.getTaskId().toString(),
                    command.getCheckListId().toString(), command.getId().toString())))));

    // returning
    return EmptyCommandResult.empty();
  }

}
