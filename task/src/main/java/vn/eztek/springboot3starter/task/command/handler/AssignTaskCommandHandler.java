package vn.eztek.springboot3starter.task.command.handler;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.NotificationSocketMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.AssignTaskSocketData;
import vn.eztek.springboot3starter.task.command.AssignTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskAssignedEvent;
import vn.eztek.springboot3starter.task.command.validator.AssignTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class AssignTaskCommandHandler implements
    CommandHandler<AssignTaskCommand, TaskResponse, TaskAggregateId> {

  private final AssignTaskCommandValidator validator;
  private final TaskRepository taskRepository;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public TaskResponse handle(AssignTaskCommand command, TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var task = taskMapper.mapToTaskBeforeUpdate(validated.getTask(), validated.getUserAssign());
    var savedTask = taskRepository.save(task);

    var event = TaskAssignedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), validated.getTask().getId().toString(),
        command.getUserId().toString());
    eventStoreService.store(event);

    //notification
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var taskId = jsonNodeFactory.textNode(validated.getTask().getId().toString());
    var assignorId = jsonNodeFactory.textNode(validated.getLoggedInUser().getId().toString());
    var metadata = jsonNodeFactory.objectNode();
    metadata.set("taskId", taskId);
    metadata.set("assignorId", assignorId);
    var userId = command.getUserId();

    redisMessagePublisher.publish(
        NotificationSocketMessage.create(SocketResponseType.TASK_ASSIGNED, metadata,
            SocketResponseType.TASK_ASSIGNED, userId));

    // send task detail to socket 
    var socketForward = SocketForward.create(SocketResponseType.TASK_ASSIGNED,
        gson.toJson(AssignTaskSocketData.create(
            savedTask.getId().toString(),
            savedTask.getAssignee().getId().toString())));

    // send project to socket
    redisMessagePublisher.publish(
        SocketEventMessage.create(validated.getTask().getProject().getId().toString(),
            SocketEventType.PROJECT, socketForward));

    // send task to socket
    redisMessagePublisher.publish(
        SocketEventMessage.create(validated.getTask().getId().toString(), SocketEventType.TASK,
            socketForward));
    // returning
    return taskMapper.mapToTaskResponse(savedTask, savedTask.getAttachments());
  }

}
