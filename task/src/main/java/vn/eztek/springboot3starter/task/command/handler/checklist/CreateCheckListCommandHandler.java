package vn.eztek.springboot3starter.task.command.handler.checklist;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.checkList.repository.CheckListRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.CheckListSocketData;
import vn.eztek.springboot3starter.task.command.checklist.CreateCheckListCommand;
import vn.eztek.springboot3starter.task.command.event.checklist.CheckListCreatedEvent;
import vn.eztek.springboot3starter.task.command.validator.checklist.CreateCheckListCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CheckListResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class CreateCheckListCommandHandler implements
    CommandHandler<CreateCheckListCommand, CheckListResponse, TaskAggregateId> {

  private final CreateCheckListCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final CheckListRepository checkListRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public CheckListResponse handle(CreateCheckListCommand command, TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var checkList = taskMapper.mapToCheckListBeforeCreate(command, command.getTaskId());

    checkList = checkListRepository.save(checkList);

    var event = CheckListCreatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), checkList.getId().toString(),
        checkList.getName());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.CHECK_LIST_ADDED, gson.toJson(
                CheckListSocketData.create(checkList.getTaskId().toString(),
                    checkList.getId().toString(), checkList.getName())))));

    // returning
    return taskMapper.mapToCheckListResponse(checkList);
  }

}
