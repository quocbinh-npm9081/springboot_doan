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
import vn.eztek.springboot3starter.shared.socket.response.UnAssignTaskSocketData;
import vn.eztek.springboot3starter.task.command.UnassignTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskUnassignedEvent;
import vn.eztek.springboot3starter.task.command.validator.UnassignTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UnassignTaskCommandHandler implements
    CommandHandler<UnassignTaskCommand, TaskResponse, TaskAggregateId> {

  private final UnassignTaskCommandValidator validator;
  private final TaskRepository taskRepository;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public TaskResponse handle(UnassignTaskCommand command, TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var assigneeId =
        validated.getTask().getAssignee() != null ? validated.getTask().getAssignee().getId()
            : null;
    var task = taskMapper.mapToTaskBeforeUpdate(validated.getTask(), null);
    var savedTask = taskRepository.save(task);

    var event = TaskUnassignedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), validated.getTask().getId().toString());
    eventStoreService.store(event);

    // send notification
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var taskId = jsonNodeFactory.textNode(validated.getTask().getId().toString());
    var metadata = jsonNodeFactory.objectNode();
    metadata.set("taskId", taskId);

    redisMessagePublisher.publish(
        NotificationSocketMessage.create(SocketResponseType.TASK_UNASSIGNED, metadata,
            SocketResponseType.TASK_UNASSIGNED, assigneeId));

    // send task detail to socket 
    var socketData = UnAssignTaskSocketData.create(task.getId().toString());
    var socketForward = SocketForward.create(SocketResponseType.TASK_UNASSIGNED,
        gson.toJson(socketData));

    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            socketForward));

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
