package vn.eztek.springboot3starter.task.command.handler;


import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.FolderDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.attachment.repository.AttachmentRepository;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.DeleteTaskSocketData;
import vn.eztek.springboot3starter.shared.socket.response.TaskSocketData;
import vn.eztek.springboot3starter.shared.util.BuildPath;
import vn.eztek.springboot3starter.task.command.DeleteTaskCommand;
import vn.eztek.springboot3starter.task.command.event.TaskDeletedEvent;
import vn.eztek.springboot3starter.task.command.validator.DeleteTaskCommandValidator;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.service.TaskEventService;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class DeleteTaskCommandHandler implements
    CommandHandler<DeleteTaskCommand, ListResponse<TaskResponse>, TaskAggregateId> {

  private final DeleteTaskCommandValidator validator;
  private final TaskMapper taskMapper;
  private final TaskRepository taskRepository;
  private final TaskEventService eventStoreService;
  private final RedisMessagePublisher redisMessagePublisher;
  private final AttachmentRepository attachmentRepository;
  private final CommentRepository commentRepository;
  private final Gson gson;

  @Override
  public ListResponse<TaskResponse> handle(DeleteTaskCommand command, TaskAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling

    var attachments = attachmentRepository.findAllByTaskId(command.getId());
    attachmentRepository.deleteAll(attachments);

    var comments = commentRepository.findAllByTaskId(command.getId());
    commentRepository.deleteAll(comments);

    var previousTask = validated.getPrevious();
    var nextTask = validated.getNext();

    if (previousTask != null) {
      previousTask.setNextId(validated.getTask().getNextId());
      taskRepository.save(previousTask);
    }

    if (nextTask != null) {
      nextTask.setPreviousId(validated.getTask().getPreviousId());
      taskRepository.save(nextTask);
    }

    taskRepository.delete(validated.getTask());

    // event storing
    var event = TaskDeletedEvent.eventOf(entityId, command.getId().toString(),
        validated.getLogginUser().getId().toString());
    eventStoreService.store(event);

    var taskDir = BuildPath.buildFolder(validated.getTask().getProject().getId().toString(),
        validated.getTask().getId().toString());

    //result
    List<TaskResponse> responses = new ArrayList<>(List.of());
    List<TaskSocketData> taskSocketData = new ArrayList<>(List.of());
    if (previousTask != null) {
      var previous = taskMapper.mapToTaskResponse(previousTask, null);
      responses.add(previous);
      taskSocketData.add(taskMapper.mapToTaskSocketData(previousTask));
    }

    if (nextTask != null) {
      var next = taskMapper.mapToTaskResponse(nextTask, null);
      responses.add(next);
      taskSocketData.add(taskMapper.mapToTaskSocketData(nextTask));
    }

    // send message to queue
    redisMessagePublisher.publish(FolderDeleteMessage.create(taskDir));

    // send project to socket
    var socketForward = SocketForward.create(SocketResponseType.TASK_DELETED,
        gson.toJson(DeleteTaskSocketData.create(command.getId().toString(), taskSocketData)));

    redisMessagePublisher.publish(
        SocketEventMessage.create(validated.getTask().getProject().getId().toString(),
            SocketEventType.PROJECT, socketForward));

    return new ListResponse<>(responses);

  }

}
