package vn.eztek.springboot3starter.task.command.handler.label;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.label.repository.LabelRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.UnAssignLabelInTaskSocketData;
import vn.eztek.springboot3starter.task.command.event.label.LabelAssignedInTaskEvent;
import vn.eztek.springboot3starter.task.command.label.UnAssignLabelInTaskCommand;
import vn.eztek.springboot3starter.task.command.validator.label.UnAssignLabelInTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.LabelResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UnAssignLabelInTaskCommandHandler implements
        CommandHandler<UnAssignLabelInTaskCommand, ListResponse<LabelResponse>, TaskAggregateId> {
  private final UnAssignLabelInTaskCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final LabelRepository labelRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public ListResponse<LabelResponse> handle(UnAssignLabelInTaskCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var labels = validated.getLabels();
    for (var label : labels) {
      label.setIsMarked(false);
      labelRepository.save(label);
    }

    var event = LabelAssignedInTaskEvent.eventOf(entityId, validated.getLoggedInUser().getId().toString()
            , command.getTaskId().toString());
    // send message to task service
    redisMessagePublisher.publish(
            SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
                    SocketForward.create(SocketResponseType.TASK_LABEL_REMOVED, gson.toJson(
                            UnAssignLabelInTaskSocketData.create(command.getTaskId().toString(), command.getId().toString())))));
    eventStoreService.store(event);

    return new ListResponse<>(labels.stream().map(taskMapper::mapToLabelResponse).toList());
  }
}
