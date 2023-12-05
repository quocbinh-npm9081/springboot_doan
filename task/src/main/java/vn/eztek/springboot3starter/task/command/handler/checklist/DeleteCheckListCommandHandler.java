package vn.eztek.springboot3starter.task.command.handler.checklist;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.checkList.repository.CheckListRepository;
import vn.eztek.springboot3starter.domain.checkListItem.repository.CheckListItemRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteCheckListSocketData;
import vn.eztek.springboot3starter.task.command.checklist.DeleteCheckListCommand;
import vn.eztek.springboot3starter.task.command.event.checklist.CheckListDeletedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklist.DeleteCheckListCommandValidator;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteCheckListCommandHandler implements
    CommandHandler<DeleteCheckListCommand, EmptyCommandResult, TaskAggregateId> {

  private final DeleteCheckListCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final CheckListRepository checkListRepository;
  private final CheckListItemRepository checkListItemRepository;

  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public EmptyCommandResult handle(DeleteCheckListCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkList = validated.getCheckList();
    checkListItemRepository.deleteAllByCheckListId(checkList.getId());
    checkListRepository.delete(checkList);

    var event = CheckListDeletedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(),
        validated.getCheckList().getId().toString());
    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_DELETED, gson.toJson(
                DeleteCheckListSocketData.create(checkList.getTaskId().toString(),
                    checkList.getId().toString())))));

    // returning
    return EmptyCommandResult.empty();
  }

}
