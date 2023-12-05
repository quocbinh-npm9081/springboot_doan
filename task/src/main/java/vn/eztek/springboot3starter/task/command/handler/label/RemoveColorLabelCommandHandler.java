package vn.eztek.springboot3starter.task.command.handler.label;

import com.google.gson.Gson;
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
import vn.eztek.springboot3starter.task.command.event.label.LabelRemovedColorEvent;
import vn.eztek.springboot3starter.task.command.label.RemoveLabelColorCommand;
import vn.eztek.springboot3starter.task.command.validator.label.RemoveColorLabelCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.LabelResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class RemoveColorLabelCommandHandler implements
        CommandHandler<RemoveLabelColorCommand, LabelResponse, TaskAggregateId> {
  private final RemoveColorLabelCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final LabelRepository labelRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public LabelResponse handle(RemoveLabelColorCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var label = validated.getLabel();
    label.setColor(null);
    label = labelRepository.save(label);

    var event = LabelRemovedColorEvent.eventOf(entityId,
            validated.getLoggedInUser().getId().toString(), label.getId().toString(),
            label.getColor());
    // send message to task
    redisMessagePublisher.publish(
            SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
                    SocketForward.create(SocketResponseType.LABEL_COLOR_REMOVED, gson.toJson(
                            LabelSocketData.create(label.getTaskId().toString(),
                                    label.getId().toString(), label.getName(), label.getColor())))));
    eventStoreService.store(event);

    return taskMapper.mapToLabelResponse(label);
  }
}
