package vn.eztek.springboot3starter.task.command.handler.comment;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.task.command.comment.UpdateCommentCommand;
import vn.eztek.springboot3starter.task.command.event.comment.CommentUpdatedEvent;
import vn.eztek.springboot3starter.task.command.validator.comment.UpdateCommentCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CommentResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class UpdateCommentCommandHandler implements
    CommandHandler<UpdateCommentCommand, CommentResponse, TaskAggregateId> {

  private final UpdateCommentCommandValidator validator;
  private final TaskMapper taskMapper;
  private final TaskEventService eventStoreService;
  private final CommentRepository commentRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public CommentResponse handle(UpdateCommentCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var comment = taskMapper.mapToCommentBeforeUpdate(validated.getComment(), command.getContent());
    comment = commentRepository.save(comment);

    // send event
    var res = taskMapper.mapToCommentResponse(comment);

    // storing event
    var event = CommentUpdatedEvent.eventOf(entityId,
        validated.getLoggedInUser().getId().toString(), command.getTaskId().toString(),
        command.getContent());
    eventStoreService.store(event);

    var socketData = taskMapper.mapToCommentSocketData(comment, comment.getLastModifiedDate());

    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
            SocketForward.create(SocketResponseType.COMMENT_UPDATED, gson.toJson(socketData))));

    // resulting
    return res;
  }
}
