package vn.eztek.springboot3starter.task.command.handler.attachment;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.FileDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.attachment.repository.AttachmentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteAttachmentSocketData;
import vn.eztek.springboot3starter.shared.util.BuildPath;
import vn.eztek.springboot3starter.task.command.attachmnet.DeleteAttachmentCommand;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentDeletedEvent;
import vn.eztek.springboot3starter.task.command.validator.attachment.DeleteAttachmentCommandValidator;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteAttachmentCommandHandler implements
    CommandHandler<DeleteAttachmentCommand, EmptyCommandResult, TaskAggregateId> {

  private final DeleteAttachmentCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final AttachmentRepository attachmentRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public EmptyCommandResult handle(DeleteAttachmentCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var attachDir = BuildPath.build(validated.getTask().getProject().getId().toString(),
        validated.getTask().getId().toString(), validated.getAttachment().getName());

    attachmentRepository.delete(validated.getAttachment());

    // event storing
    var event = AttachmentDeletedEvent.eventOf(entityId,
        validated.getLogginUser().getId().toString(), command.getTaskId().toString(),
        command.getAttachmentId().toString());

    eventStoreService.store(event);

    // send message to delete file
    redisMessagePublisher.publish(FileDeleteMessage.create(attachDir));

    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.ATTACHMENT_DELETED, gson.toJson(
                DeleteAttachmentSocketData.create(command.getAttachmentId().toString())))));

    return EmptyCommandResult.empty();
  }
}
