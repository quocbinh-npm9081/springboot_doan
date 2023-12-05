package vn.eztek.springboot3starter.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import vn.eztek.springboot3starter.domain.attachment.entity.Attachment;
import vn.eztek.springboot3starter.domain.checkList.entity.CheckList;
import vn.eztek.springboot3starter.domain.checkListItem.entity.CheckListItem;
import vn.eztek.springboot3starter.domain.comment.entity.Comment;
import vn.eztek.springboot3starter.domain.label.entity.Label;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.socket.response.*;
import vn.eztek.springboot3starter.task.command.checklist.CreateCheckListCommand;
import vn.eztek.springboot3starter.task.command.label.CreateLabelCommand;
import vn.eztek.springboot3starter.task.response.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskMapper {

  @Mappings({@Mapping(target = "assigneeId", source = "task.assignee.id"),
          @Mapping(target = "projectId", source = "task.project.id"),
          @Mapping(target = "stageId", source = "task.stage.id"),
          @Mapping(target = "ownerId", source = "task.owner.id"),
          @Mapping(target = "attachments", source = "attachments"),})
  public abstract TaskResponse mapToTaskResponse(Task task, List<Attachment> attachments);

  @Mappings({@Mapping(target = "assigneeId", source = "task.assignee.id"),
          @Mapping(target = "projectId", source = "task.project.id"),
          @Mapping(target = "stageId", source = "task.stage.id"),
          @Mapping(target = "ownerId", source = "task.owner.id"),})
  public abstract TaskDetailSocketData mapToTaskDetailSocketData(Task task);

  @Mappings({@Mapping(target = "stageId", source = "task.stage.id"),})
  public abstract TaskSocketData mapToTaskSocketData(Task task);

  @Mappings({@Mapping(target = "assigneeId", source = "task.assignee.id"),
          @Mapping(target = "stageId", source = "task.stage.id"),
          @Mapping(target = "ownerId", source = "task.owner.id")})
  public abstract SimplifyTaskResponse mapToSimplifyTaskResponse(Task task, Boolean hasDescription);

  public abstract UpdateTaskSocketData mapToUpdateTaskSocketData(Task task);

  @Mappings({@Mapping(target = "id", source = "task.id"),
          @Mapping(target = "createdBy", source = "task.createdBy"),
          @Mapping(target = "createdDate", source = "task.createdDate"),
          @Mapping(target = "lastModifiedBy", source = "task.lastModifiedBy"),
          @Mapping(target = "lastModifiedDate", source = "task.lastModifiedDate"),
          @Mapping(target = "assignee", source = "assignee"),})
  public abstract Task mapToTaskBeforeUpdate(Task task, User assignee);

  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "createdBy", ignore = true),
          @Mapping(target = "stage", source = "stage"), @Mapping(target = "createdDate", ignore = true),
          @Mapping(target = "description", source = "description"),
          @Mapping(target = "lastModifiedBy", ignore = true),
          @Mapping(target = "lastModifiedDate", ignore = true),})
  public abstract Task mapToTaskBeforeCreate(String title, String description, Project project,
                                             Stage stage, User owner, UUID nextId, UUID previousId);

  @Mappings({@Mapping(target = "id", source = "task.id"),
          @Mapping(target = "createdBy", source = "task.createdBy"),
          @Mapping(target = "createdDate", source = "task.createdDate"),
          @Mapping(target = "lastModifiedBy", source = "task.lastModifiedBy"),
          @Mapping(target = "lastModifiedDate", source = "task.lastModifiedDate"),
          @Mapping(target = "project", source = "task.project"),
          @Mapping(target = "stage", source = "stage"),
          @Mapping(target = "previousId", source = "previousId"),
          @Mapping(target = "nextId", source = "nextId"),

  })
  public abstract Task mapToTaskBeforeBatchUpdate(Task task, Stage stage, UUID previousId,
                                                  UUID nextId);

  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "createdBy", ignore = true),
          @Mapping(target = "createdDate", ignore = true),
          @Mapping(target = "lastModifiedBy", ignore = true),
          @Mapping(target = "lastModifiedDate", ignore = true),
          @Mapping(target = "task", source = "task"),})
  public abstract Attachment mapToAttachmentBeforeCreate(String name, String originalName,
                                                         Task task);

  @Mappings({@Mapping(target = "originalName", source = "originalName"),})
  public abstract Attachment mapToAttachmentBeforeUpdate(Attachment attachment,
                                                         String originalName);

  @Mappings({@Mapping(target = "id", source = "attachment.id"),
          @Mapping(target = "name", source = "attachment.name"),
          @Mapping(target = "originalName", source = "attachment.originalName"),
          @Mapping(target = "taskId", source = "task.id"),})
  public abstract AttachmentResponse mapToAttachmentResponse(Attachment attachment);

  @Mappings({@Mapping(target = "id", source = "attachment.id"),
          @Mapping(target = "name", source = "attachment.name"),
          @Mapping(target = "originalName", source = "attachment.originalName"),
          @Mapping(target = "taskId", source = "task.id"),})
  public abstract AttachmentSocketData mapToAttachmentSocketData(Attachment attachment);

  @Mappings({@Mapping(target = "content", source = "content"),
          @Mapping(target = "user", source = "user"), @Mapping(target = "taskId", source = "taskId"),})
  public abstract Comment mapToCommentBeforeCreate(String content, UUID taskId, User user,
                                                   UUID parentCommentId);

  @Mappings({@Mapping(target = "content", source = "content"),})
  public abstract Comment mapToCommentBeforeUpdate(Comment comment, String content);

  @Mappings({@Mapping(target = "user.email", source = "comment.user.username"),})
  public abstract CommentResponse mapToCommentResponse(Comment comment);

  @Mappings({@Mapping(target = "user.email", source = "comment.user.username"),})
  public abstract CommentSocketData mapToCommentSocketData(Comment comment,
                                                           ZonedDateTime lastModifiedDate);

  public abstract CheckList mapToCheckListBeforeCreate(CreateCheckListCommand command, UUID taskId);

  @Mappings({@Mapping(target = "name", source = "name"),})
  public abstract CheckList mapToCheckListBeforeUpdate(CheckList checkList, String name);

  public abstract CheckListResponse mapToCheckListResponse(CheckList checkList);

  @Mappings({@Mapping(target = "id", ignore = true),
          @Mapping(target = "checkList", source = "checkList"),
  })
  public abstract CheckListItem mapToCheckListItemBeforeCreate(String content, CheckList checkList);

  @Mappings({@Mapping(target = "content", source = "content"),})
  public abstract CheckListItem mapToCheckListItemBeforeUpdate(CheckListItem checkListItem,
                                                               String content);

  @Mappings({@Mapping(target = "id", source = "checkListItem.id"),
          @Mapping(target = "assignee", source = "checkListItem.assignee"),
          @Mapping(target = "checkListId", source = "checkListItem.checkList.id"),
          @Mapping(target = "assignee.email", source = "checkListItem.assignee.username"),})
  public abstract CheckListItemResponse mapToCheckListItemResponse(CheckListItem checkListItem);

  public abstract Label mapToLabelBeforeCreate(CreateLabelCommand command, UUID taskId, String color);

  @Mappings({@Mapping(target = "name", source = "name"),
          @Mapping(target = "color", source = "color"),
          @Mapping(target = "isMarked", source = "isMarked")
  })
  public abstract LabelResponse mapToLabelResponse(Label label);


  @Mappings({@Mapping(target = "name", source = "name"),
          @Mapping(target = "color", source = "color")
  })
  public abstract Label mapToLabelBeforeUpdate(Label label, String name, String color);


}
