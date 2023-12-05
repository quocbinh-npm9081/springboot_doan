package vn.eztek.springboot3starter.task.command.handler.comment;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteCommentSocketData;
import vn.eztek.springboot3starter.task.command.comment.DeleteCommentCommand;
import vn.eztek.springboot3starter.task.command.event.comment.CommentDeletedEvent;
import vn.eztek.springboot3starter.task.command.validator.comment.DeleteCommentCommandValidator;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteCommentCommandHandler implements
    CommandHandler<DeleteCommentCommand, EmptyCommandResult, TaskAggregateId> {

  private final DeleteCommentCommandValidator validator;
  private final TaskEventService eventStoreService;
  private final CommentRepository commentRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public EmptyCommandResult handle(DeleteCommentCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    // delete all children comment
    var childrenComment = commentRepository.findByParentCommentId(command.getCommentId());
    if (!childrenComment.isEmpty()) {
      commentRepository.deleteAll(childrenComment);
    }
    commentRepository.delete(validated.getComment());
    if (validated.getParentComment() != null) {
      validated.getParentComment().setCountReply(validated.getParentComment().getCountReply() - 1);
      commentRepository.save(validated.getParentComment());
    }

    // event storing
    var event = CommentDeletedEvent.eventOf(entityId, validated.getLogginUser().getId().toString(),
        command.getTaskId().toString(), command.getCommentId().toString());

    eventStoreService.store(event);

    // send message to task
    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.COMMENT_DELETED,
                gson.toJson(DeleteCommentSocketData.create(command.getCommentId().toString())))));

    return EmptyCommandResult.empty();
  }
}
