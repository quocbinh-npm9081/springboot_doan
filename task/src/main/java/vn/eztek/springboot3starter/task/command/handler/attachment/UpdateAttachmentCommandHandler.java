package vn.eztek.springboot3starter.task.command.handler.attachment;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.attachment.repository.AttachmentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.UpdateAttachmentSocketData;
import vn.eztek.springboot3starter.task.command.attachmnet.UpdateAttachmentCommand;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.attachment.UpdateAttachmentCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.AttachmentResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateAttachmentCommandHandler implements
    CommandHandler<UpdateAttachmentCommand, AttachmentResponse, TaskAggregateId> {

  private final UpdateAttachmentCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final AttachmentRepository attachmentRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public AttachmentResponse handle(UpdateAttachmentCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var attachment = taskMapper.mapToAttachmentBeforeUpdate(validated.getAttachment(),
        command.getName());

    attachment = attachmentRepository.save(attachment);

    var event = AttachmentUpdatedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), attachment.getId().toString(),
        attachment.getOriginalName());
    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.ATTACHMENT_UPDATED, gson.toJson(
                UpdateAttachmentSocketData.create(attachment.getId(),
                    attachment.getOriginalName())))));

    // returning
    return taskMapper.mapToAttachmentResponse(attachment);
  }

}
