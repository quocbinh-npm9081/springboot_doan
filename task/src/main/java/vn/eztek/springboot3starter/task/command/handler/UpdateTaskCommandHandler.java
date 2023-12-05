package vn.eztek.springboot3starter.task.command.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import java.util.UUID;
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
import vn.eztek.springboot3starter.task.command.UpdateTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.UpdateTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateTaskCommandHandler implements
    CommandHandler<UpdateTaskCommand, TaskResponse, TaskAggregateId> {

  private final UpdateTaskCommandValidator validator;
  private final TaskRepository userRepository;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final ObjectMapper objectMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public TaskResponse handle(UpdateTaskCommand command, TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var task = userRepository.save(validated.getTask());

    var event = TaskUpdatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), validated.getTask().getId().toString(),
        command.getInput());
    eventStoreService.store(event);

    //notification
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var userId = jsonNodeFactory.textNode(validated.getLoggedInUser().getId().toString());
    var metadata = objectMapper.createObjectNode();
    metadata.set("userId", userId);

    // send socket notification owner
    UUID ownerId = null;
    if (validated.getTask().getOwner() != null && !validated.getTask().getOwner().getId()
        .equals(validated.getLoggedInUser().getId())) {
      ownerId = validated.getTask().getOwner().getId();

      redisMessagePublisher.publish(
          NotificationSocketMessage.create(SocketResponseType.TASK_UPDATED, metadata,
              SocketResponseType.TASK_UPDATED, ownerId));
    }

    // send socket notification assignee
    if (validated.getTask().getAssignee() != null && !validated.getTask().getAssignee().getId()
        .equals(ownerId)) {
      var assigneeId = jsonNodeFactory.textNode(
          validated.getTask().getAssignee().getId().toString());
      metadata = objectMapper.createObjectNode();
      metadata.set("userId", assigneeId);
      redisMessagePublisher.publish(
          NotificationSocketMessage.create(SocketResponseType.TASK_UPDATED, metadata,
              SocketResponseType.TASK_UPDATED, validated.getTask().getAssignee().getId()));
    }

    var taskSocket = taskMapper.mapToUpdateTaskSocketData(task);
    var socketForward = SocketForward.create(SocketResponseType.TASK_UPDATED,
        gson.toJson(taskSocket));

    // send task to socket
    redisMessagePublisher.publish(
        SocketEventMessage.create(validated.getTask().getId().toString(), SocketEventType.TASK,
            socketForward));

    // send project to socket
    taskSocket.setDescription(null);
    taskSocket.setHasDescription(task.getDescription() != null && !task.getDescription().isBlank());
    socketForward = SocketForward.create(SocketResponseType.TASK_UPDATED, gson.toJson(taskSocket));
    redisMessagePublisher.publish(
        SocketEventMessage.create(validated.getTask().getProject().getId().toString(),
            SocketEventType.PROJECT, socketForward));

    // returning
    return taskMapper.mapToTaskResponse(task, task.getAttachments());
  }

}
