package vn.eztek.springboot3starter.task.command.handler.attachment;


import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.common.storage.StorageService;
import vn.eztek.springboot3starter.domain.attachment.repository.AttachmentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.util.BuildPath;
import vn.eztek.springboot3starter.task.command.attachmnet.UploadAttachmentCommand;
import vn.eztek.springboot3starter.task.command.event.attachment.AttachmentUploadedEvent;
import vn.eztek.springboot3starter.task.command.validator.attachment.UploadAttachmentCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.AttachmentResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UploadAttachmentCommandHandler implements
    CommandHandler<UploadAttachmentCommand, AttachmentResponse, TaskAggregateId> {

  private final UploadAttachmentCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final TaskMapper taskMapper;
  private final AttachmentRepository attachmentRepository;
  private final StorageService storageService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  @Transactional
  public AttachmentResponse handle(UploadAttachmentCommand command,
      TaskAggregateId taskAggregateId) {

    // validating
    var validated = validator.validate(command);

    // handling

    String blobName = BuildPath.build(validated.getTask().getProject().getId().toString(),
        validated.getTask().getId().toString(), validated.getName());

    storageService.uploadFile(blobName, command.getAttachment());

    var attachment = taskMapper.mapToAttachmentBeforeCreate(validated.getName(),
        command.getAttachment().getOriginalFilename(), validated.getTask());
    attachment = attachmentRepository.save(attachment);

    var event = AttachmentUploadedEvent.eventOf(taskAggregateId,
        validated.getLoggedInUser().getId().toString(), validated.getTask().getId().toString(),
        attachment.getId().toString(), attachment.getOriginalName(), attachment.getName());
    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.ATTACHMENT_ADDED, gson.toJson(
                taskMapper.mapToAttachmentSocketData(attachment)))));

    // returning
    return taskMapper.mapToAttachmentResponse(attachment);
  }

}
