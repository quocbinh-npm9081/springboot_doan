package vn.eztek.springboot3starter.task.command.handler.comment;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.NotificationSocketMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.task.command.comment.PostCommentCommand;
import vn.eztek.springboot3starter.task.command.event.comment.CommentPostedEvent;
import vn.eztek.springboot3starter.task.command.validator.comment.PostCommentCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.CommentResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class PostCommentCommandHandler implements
    CommandHandler<PostCommentCommand, CommentResponse, TaskAggregateId> {

  private final PostCommentCommandValidator validator;
  private final TaskMapper taskMapper;
  private final TaskEventService eventStoreService;
  private final CommentRepository commentRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public CommentResponse handle(PostCommentCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var comment = taskMapper.mapToCommentBeforeCreate(command.getContent(), command.getTaskId(),
        validated.getLoggedInUser(), command.getParentId());

    comment = commentRepository.save(comment);

    var parentComment = validated.getParentComment();

    if (parentComment != null) {
      parentComment.setCountReply(parentComment.getCountReply() + 1);
      commentRepository.save(parentComment);
    }

    var res = taskMapper.mapToCommentResponse(comment);

    // storing event
    var event = CommentPostedEvent.eventOf(entityId, validated.getLoggedInUser().getId().toString(),
        command.getTaskId().toString(), command.getContent());
    eventStoreService.store(event);

    //notification
    // comment on task
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
    var metadata = jsonNodeFactory.objectNode();
    var socketForward = taskMapper.mapToCommentSocketData(comment, null);
    if (validated.getParentComment() == null) {
      if (validated.getTask().getOwner() != null) {
        var taskId = jsonNodeFactory.textNode(validated.getTask().getId().toString());
        var commentId = jsonNodeFactory.textNode(comment.getId().toString());
        var userId = jsonNodeFactory.textNode(comment.getUser().getId().toString());
        var ownerId = validated.getTask().getOwner().getId();

        metadata.set("taskId", taskId);
        metadata.set("commentId", commentId);
        metadata.set("userId", userId);

        // send notification
        redisMessagePublisher.publish(
            NotificationSocketMessage.create(SocketResponseType.COMMENT_ADDED, metadata,
                SocketResponseType.COMMENT_ADDED, ownerId));
      }

      // send task to socket
      redisMessagePublisher.publish(
          SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
              SocketForward.create(SocketResponseType.COMMENT_ADDED, gson.toJson(socketForward))));
    }
    // reply comment
    else {
      if (validated.getParentComment().getUser() != null) {
        var parentUserCommentId = validated.getParentComment().getUser().getId();
        var parentUserId = jsonNodeFactory.textNode(parentUserCommentId.toString());
        var userId = jsonNodeFactory.textNode(comment.getUser().getId().toString());
        metadata.set("userId", userId);
        metadata.set("parentUserId", parentUserId);

        redisMessagePublisher.publish(
            NotificationSocketMessage.create(SocketResponseType.COMMENT_REPLIED, metadata,
                SocketResponseType.COMMENT_REPLIED, parentUserCommentId));
      }

      redisMessagePublisher.publish(
          SocketEventMessage.create(command.getTaskId().toString(), SocketEventType.TASK,
              SocketForward.create(SocketResponseType.COMMENT_REPLIED,
                  gson.toJson(socketForward))));

    }
    // resulting
    return res;
  }

}
