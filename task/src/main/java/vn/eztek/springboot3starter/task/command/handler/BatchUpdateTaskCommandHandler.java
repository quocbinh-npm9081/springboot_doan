package vn.eztek.springboot3starter.task.command.handler;

import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.ListTaskSocketData;
import vn.eztek.springboot3starter.task.command.BatchUpdateTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskBatchUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.BatchUpdateTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.request.BatchUpdateTaskRequest;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class BatchUpdateTaskCommandHandler implements
    CommandHandler<BatchUpdateTaskCommand, ListResponse<TaskResponse>, TaskAggregateId> {

  private final BatchUpdateTaskCommandValidator validator;
  private final TaskRepository taskRepository;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public ListResponse<TaskResponse> handle(BatchUpdateTaskCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    List<Task> taskList = new ArrayList<>();
    for (Map.Entry<Task, Stage> entry : validated.getTasks().entrySet()) {
      // find index of task in list
      BatchUpdateTaskRequest taskRequest = command.getTasks().stream()
          .filter(task -> task.getId().equals(entry.getKey().getId())).findFirst().get();

      var task = taskMapper.mapToTaskBeforeBatchUpdate(entry.getKey(), entry.getValue(),
          taskRequest.getPreTaskId(), taskRequest.getNextTaskId());
      taskList.add(task);
    }

    taskList = taskRepository.saveAll(taskList);

    var event = TaskBatchUpdatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), command.getTasks());
    eventStoreService.store(event);

    var res = taskList.stream().map(task -> taskMapper.mapToTaskResponse(task, null)).toList();

    // send project to socket
    var socketsData = taskList.stream().map(taskMapper::mapToTaskSocketData).toList();
    var socketResponse = SocketForward.create(SocketResponseType.TASK_POSITION_UPDATED,
        gson.toJson(new ListTaskSocketData(socketsData)));

    redisMessagePublisher.publish(
        SocketEventMessage.create(taskList.get(0).getProject().getId().toString(),
            SocketEventType.PROJECT, socketResponse));

    // returning
    return new ListResponse<>(res);
  }

}
