package vn.eztek.springboot3starter.task.command.handler;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.CreateTaskSocketData;
import vn.eztek.springboot3starter.shared.socket.response.TaskSocketData;
import vn.eztek.springboot3starter.task.command.CreateTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskCreatedEvent;
import vn.eztek.springboot3starter.task.command.validator.CreateTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class CreateTaskCommandHandler implements
    CommandHandler<CreateTaskCommand, ListResponse<TaskResponse>, TaskAggregateId> {

  private final CreateTaskCommandValidator validator;
  private final TaskMapper taskMapper;
  private final TaskRepository taskRepository;
  private final TaskEventService eventStoreService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public ListResponse<TaskResponse> handle(CreateTaskCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var task = taskMapper.mapToTaskBeforeCreate(command.getTitle(), command.getDescription(),
        validated.getProject(), validated.getStage(), validated.getOwner(), command.getNextId(),
        command.getPreviousId());

    task = taskRepository.save(task);

    Task taskPrevious = validated.getPrevious();
    Task taskNext = validated.getNext();

    if (command.getPreviousId() != null) {
      taskPrevious.setNextId(task.getId());
      taskPrevious = taskRepository.save(taskPrevious);
    }

    if (command.getNextId() != null) {
      taskNext.setPreviousId(task.getId());
      taskNext = taskRepository.save(taskNext);
    }

    // event storing
    var event = TaskCreatedEvent.eventOf(entityId, validated.getOwner().getId().toString(),
        task.getId().toString(), task.getTitle(), task.getDescription(),
        validated.getProject().getId(), validated.getStage().getId(), command.getPreviousId(),
        command.getNextId());
    eventStoreService.store(event);

    //result
    List<TaskResponse> taskResponses = new ArrayList<>();
    List<TaskSocketData> taskSockets = new ArrayList<>();

    if (command.getPreviousId() != null) {
      var previous = taskMapper.mapToTaskResponse(taskPrevious, null);
      taskSockets.add(taskMapper.mapToTaskSocketData(taskPrevious));
      taskResponses.add(previous);
    }
    var middle = taskMapper.mapToTaskResponse(task, null);

    taskResponses.add(middle);
    if (command.getNextId() != null) {
      var next = taskMapper.mapToTaskResponse(taskNext, null);
      taskResponses.add(next);
      taskSockets.add(taskMapper.mapToTaskSocketData(taskNext));
    }

    // send socket to project
    var createdTaskSocket = taskMapper.mapToTaskDetailSocketData(task);
    var socketForward = SocketForward.create(SocketResponseType.TASK_ADDED,
        gson.toJson(CreateTaskSocketData.create(createdTaskSocket, taskSockets)));

    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getProjectId().toString(), SocketEventType.PROJECT,
            socketForward));

    return new ListResponse<>(taskResponses);

  }
}
