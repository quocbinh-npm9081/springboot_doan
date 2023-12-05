package vn.eztek.springboot3starter.task.command.handler.label;

import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.label.repository.LabelRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteLabelSocketData;
import vn.eztek.springboot3starter.task.command.event.label.LabelDeletedEvent;
import vn.eztek.springboot3starter.task.command.label.DeleteLabelCommand;
import vn.eztek.springboot3starter.task.command.validator.label.DeleteLabelCommandValidator;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteLabelCommandHandler implements
        CommandHandler<DeleteLabelCommand, EmptyCommandResult, TaskAggregateId> {
  private final DeleteLabelCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final LabelRepository labelRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public EmptyCommandResult handle(DeleteLabelCommand command,
                                   TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var label = validated.getLabel();
    labelRepository.delete(label);

    var event = LabelDeletedEvent.eventOf(taskAggregateId,
            validated.getLoggedInUser().getId().toString(),
            validated.getLabel().getId().toString());
    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
            SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
                    SocketForward.create(SocketResponseType.CHECK_LIST_DELETED, gson.toJson(
                            DeleteLabelSocketData.create(label.getTaskId().toString(),
                                    label.getId().toString())))));

    // returning
    return EmptyCommandResult.empty();
  }
}
