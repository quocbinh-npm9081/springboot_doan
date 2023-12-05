package vn.eztek.springboot3starter.project.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.project.command.AddMembersToProjectCommand;
import vn.eztek.springboot3starter.project.command.CreateProjectCommand;
import vn.eztek.springboot3starter.project.command.CreateSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.DeleteProjectCommand;
import vn.eztek.springboot3starter.project.command.DeleteSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.RestoreProjectCommand;
import vn.eztek.springboot3starter.project.command.UpdateProjectCommand;
import vn.eztek.springboot3starter.project.command.UpdateStagesCommand;
import vn.eztek.springboot3starter.project.command.UpdateStatusMemberInProjectCommand;
import vn.eztek.springboot3starter.project.command.handler.AddMembersToProjectCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.CreateProjectCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.CreateSharedLinkCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.DeleteProjectCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.DeleteSharedLinkCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.RestoreProjectCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.UpdateProjectCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.UpdateStagesCommandHandler;
import vn.eztek.springboot3starter.project.command.handler.UpdateStatusMemberInProjectCommandHandler;
import vn.eztek.springboot3starter.project.query.GetMyArchiveProjectQuery;
import vn.eztek.springboot3starter.project.query.GetMyProjectQuery;
import vn.eztek.springboot3starter.project.query.GetProjectByIdQuery;
import vn.eztek.springboot3starter.project.query.GetSharedLinkQuery;
import vn.eztek.springboot3starter.project.query.ListProjectMembersQuery;
import vn.eztek.springboot3starter.project.query.ListProjectQuery;
import vn.eztek.springboot3starter.project.query.ListStagesQuery;
import vn.eztek.springboot3starter.project.query.handler.GetMyArchiveProjectQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.GetMyProjectQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.GetProjectByIdQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.GetSharedLinkQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.ListProjectMembersQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.ListProjectQueryHandler;
import vn.eztek.springboot3starter.project.query.handler.ListStagesQueryHandler;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class ProjectAggregate extends AggregateRoot<ProjectAggregate, ProjectAggregateId> {

  public ProjectAggregate(ApplicationContext applicationContext) {
    super(applicationContext, new ProjectAggregateId());
  }

  @Override
  protected AggregateRootBehavior initialBehavior() {
    var behaviorBuilder = new AggregateRootBehaviorBuilder();

    behaviorBuilder.setCommandHandler(CreateProjectCommand.class,
        getCommandHandler(CreateProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateProjectCommand.class,
        getCommandHandler(UpdateProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(AddMembersToProjectCommand.class,
        getCommandHandler(AddMembersToProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateStagesCommand.class,
        getCommandHandler(UpdateStagesCommandHandler.class));
    behaviorBuilder.setCommandHandler(UpdateStatusMemberInProjectCommand.class,
        getCommandHandler(UpdateStatusMemberInProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteProjectCommand.class,
        getCommandHandler(DeleteProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(RestoreProjectCommand.class,
        getCommandHandler(RestoreProjectCommandHandler.class));
    behaviorBuilder.setCommandHandler(CreateSharedLinkCommand.class,
        getCommandHandler(CreateSharedLinkCommandHandler.class));
    behaviorBuilder.setCommandHandler(DeleteSharedLinkCommand.class,
        getCommandHandler(DeleteSharedLinkCommandHandler.class));

    behaviorBuilder.setQueryHandler(GetProjectByIdQuery.class,
        getQueryHandler(GetProjectByIdQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetMyProjectQuery.class,
        getQueryHandler(GetMyProjectQueryHandler.class));
    behaviorBuilder.setQueryHandler(ListProjectQuery.class,
        getQueryHandler(ListProjectQueryHandler.class));
    behaviorBuilder.setQueryHandler(ListProjectMembersQuery.class,
        getQueryHandler(ListProjectMembersQueryHandler.class));
    behaviorBuilder.setQueryHandler(ListStagesQuery.class,
        getQueryHandler(ListStagesQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetMyArchiveProjectQuery.class,
        getQueryHandler(GetMyArchiveProjectQueryHandler.class));
    behaviorBuilder.setQueryHandler(GetSharedLinkQuery.class,
        getQueryHandler(GetSharedLinkQueryHandler.class));

    return behaviorBuilder.build();
  }

  @Override
  public boolean sameIdentityAs(ProjectAggregate other) {
    return other != null && entityId.sameValueAs(other.entityId);
  }

  @Override
  public ProjectAggregateId id() {
    return entityId;
  }
}
