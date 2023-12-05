package vn.eztek.springboot3starter.task.command.handler.label;

import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.label.repository.LabelRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.LabelSocketData;
import vn.eztek.springboot3starter.task.command.event.label.LabelCreatedEvent;
import vn.eztek.springboot3starter.task.command.label.CreateLabelCommand;
import vn.eztek.springboot3starter.task.command.validator.label.CreateLabelCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.LabelResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class CreateLabelCommandHandler implements
        CommandHandler<CreateLabelCommand, LabelResponse, TaskAggregateId> {
  private final CreateLabelCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final LabelRepository labelRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public LabelResponse handle(CreateLabelCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var label = taskMapper.mapToLabelBeforeCreate(command, command.getTaskId(), command.getColor());

    label = labelRepository.save(label);

    var event = LabelCreatedEvent.eventOf(entityId,
            validated.getLoggedInUser().getId().toString(), label.getId().toString(),
            label.getName(), label.getColor());
    // send message to task
    redisMessagePublisher.publish(
            SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
                    SocketForward.create(SocketResponseType.LABEL_ADDED, gson.toJson(
                            LabelSocketData.create(label.getTaskId().toString(),
                                    label.getId().toString(), label.getName(), label.getColor())))));
    eventStoreService.store(event);

    return taskMapper.mapToLabelResponse(label);
  }
}
