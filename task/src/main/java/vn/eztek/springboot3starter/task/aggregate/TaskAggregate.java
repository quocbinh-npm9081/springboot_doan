package vn.eztek.springboot3starter.task.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;
import vn.eztek.springboot3starter.task.command.*;
import vn.eztek.springboot3starter.task.command.attachmnet.DeleteAttachmentCommand;
import vn.eztek.springboot3starter.task.command.attachmnet.UpdateAttachmentCommand;
import vn.eztek.springboot3starter.task.command.attachmnet.UploadAttachmentCommand;
import vn.eztek.springboot3starter.task.command.checklist.CreateCheckListCommand;
import vn.eztek.springboot3starter.task.command.checklist.DeleteCheckListCommand;
import vn.eztek.springboot3starter.task.command.checklist.UpdateCheckListCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.*;
import vn.eztek.springboot3starter.task.command.comment.DeleteCommentCommand;
import vn.eztek.springboot3starter.task.command.comment.PostCommentCommand;
import vn.eztek.springboot3starter.task.command.comment.UpdateCommentCommand;
import vn.eztek.springboot3starter.task.command.handler.*;
import vn.eztek.springboot3starter.task.command.handler.attachment.DeleteAttachmentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.attachment.UpdateAttachmentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.attachment.UploadAttachmentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.checklist.CreateCheckListCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.checklist.DeleteCheckListCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.checklist.UpdateCheckListCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.checklistitem.*;
import vn.eztek.springboot3starter.task.command.handler.comment.DeleteCommentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.comment.PostCommentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.comment.UpdateCommentCommandHandler;
import vn.eztek.springboot3starter.task.command.handler.label.*;
import vn.eztek.springboot3starter.task.command.label.*;
import vn.eztek.springboot3starter.task.query.GetChildrenCommentQuery;
import vn.eztek.springboot3starter.task.query.GetCommentByTaskIdQuery;
import vn.eztek.springboot3starter.task.query.GetTaskByIdQuery;
import vn.eztek.springboot3starter.task.query.ListTasksQuery;
import vn.eztek.springboot3starter.task.query.checklist.GetCheckListByTaskIdQuery;
import vn.eztek.springboot3starter.task.query.handler.GetChildrenCommentQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.GetCommentByTaskIdQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.GetTaskByIdQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.ListTasksQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.checklist.GetCheckListByTaskIdQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.label.GetLabelByTaskIdAndIsMarkedTrueQueryHandler;
import vn.eztek.springboot3starter.task.query.handler.label.GetLabelByTaskIdQueryHandler;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdAndIsMarkedTrueQuery;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdQuery;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

public class TaskAggregate extends AggregateRoot<TaskAggregate, TaskAggregateId> {

  public TaskAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new TaskAggregateId());
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    // task
    //// command
    behaviorBuilder.setCommandHandler(CreateTaskCommand.class,
            getCommandHandler(CreateTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateTaskCommand.class,
            getCommandHandler(UpdateTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(AssignTaskCommand.class,
            getCommandHandler(AssignTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(BatchUpdateTaskCommand.class,
            getCommandHandler(BatchUpdateTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteTaskCommand.class,
            getCommandHandler(DeleteTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(UnassignTaskCommand.class,
            getCommandHandler(UnassignTaskCommandHandler.class));
    //// query
    behaviorBuilder.setQueryHandler(GetTaskByIdQuery.class,
            getQueryHandler(GetTaskByIdQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetTaskByIdQuery.class,
            getQueryHandler(GetTaskByIdQueryHandler.class));
    behaviorBuilder.setQueryHandler(ListTasksQuery.class,
            getQueryHandler(ListTasksQueryHandler.class));

    // comment
    //// command
    behaviorBuilder.setCommandHandler(PostCommentCommand.class,
            getCommandHandler(PostCommentCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateCommentCommand.class,
            getCommandHandler(UpdateCommentCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteCommentCommand.class,
            getCommandHandler(DeleteCommentCommandHandler.class));
    //// query
    behaviorBuilder.setQueryHandler(GetChildrenCommentQuery.class,
            getQueryHandler(GetChildrenCommentQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetCommentByTaskIdQuery.class,
            getQueryHandler(GetCommentByTaskIdQueryHandler.class));

    // attachment
    //// command
    behaviorBuilder.setCommandHandler(UploadAttachmentCommand.class,
            getCommandHandler(UploadAttachmentCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteAttachmentCommand.class,
            getCommandHandler(DeleteAttachmentCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateAttachmentCommand.class,
            getCommandHandler(UpdateAttachmentCommandHandler.class));

    // check list
    //// command
    behaviorBuilder.setCommandHandler(CreateCheckListCommand.class,
            getCommandHandler(CreateCheckListCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateCheckListCommand.class,
            getCommandHandler(UpdateCheckListCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteCheckListCommand.class,
            getCommandHandler(DeleteCheckListCommandHandler.class));
    //// query
    behaviorBuilder.setQueryHandler(GetCheckListByTaskIdQuery.class,
            getQueryHandler(GetCheckListByTaskIdQueryHandler.class));

    // check list item
    //// command
    behaviorBuilder.setCommandHandler(AssignCheckListItemCommand.class,
            getCommandHandler(AssignCheckListItemCommandHandler.class));
    behaviorBuilder.setCommandHandler(UnAssignCheckListItemCommand.class,
            getCommandHandler(UnAssignCheckListItemCommandHandler.class));
    behaviorBuilder.setCommandHandler(CreateCheckListItemCommand.class,
            getCommandHandler(CreateCheckListItemCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteCheckListItemCommand.class,
            getCommandHandler(DeleteCheckListItemCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateCheckListItemCommand.class,
            getCommandHandler(UpdateCheckListItemCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateDueDateCommand.class,
            getCommandHandler(UpdateDueDateCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateStatusCheckListItemCommand.class,
            getCommandHandler(UpdateStatusCheckListItemCommandHandler.class));

    //label
    ///command
    behaviorBuilder.setCommandHandler(CreateLabelCommand.class,
            getCommandHandler(CreateLabelCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateLabelCommand.class,
            getCommandHandler(UpdateLabelCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteLabelCommand.class,
            getCommandHandler(DeleteLabelCommandHandler.class));
    behaviorBuilder.setCommandHandler(AssignLabelInTaskCommand.class,
            getCommandHandler(AssignLabelInTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(UnAssignLabelInTaskCommand.class,
            getCommandHandler(UnAssignLabelInTaskCommandHandler.class));
    behaviorBuilder.setCommandHandler(RemoveLabelColorCommand.class,
            getCommandHandler(RemoveColorLabelCommandHandler.class));

    //label
    ///query
    behaviorBuilder.setQueryHandler(GetLabelByTaskIdQuery.class,
            getQueryHandler(GetLabelByTaskIdQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetLabelByTaskIdAndIsMarkedTrueQuery.class,
            getQueryHandler(GetLabelByTaskIdAndIsMarkedTrueQueryHandler.class));
    return behaviorBuilder.build();
  }

  @Override
  public boolean sameIdentityAs(TaskAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public TaskAggregateId id() {
    return entityId;
  }
}
