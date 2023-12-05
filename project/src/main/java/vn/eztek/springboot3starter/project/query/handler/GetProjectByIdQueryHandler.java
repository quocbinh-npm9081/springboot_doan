package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.GetProjectByIdQuery;
import vn.eztek.springboot3starter.project.query.validator.GetProjectByIdQueryValidator;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;

@Component
@RequiredArgsConstructor
public class GetProjectByIdQueryHandler
    implements QueryHandler<GetProjectByIdQuery, ProjectResponse, ProjectAggregateId> {

  private final GetProjectByIdQueryValidator validator;
  private final ProjectMapper projectMapper;

  @Override
  public ProjectResponse handle(GetProjectByIdQuery query, ProjectAggregateId entityId) {

    var validated = validator.validate(query);

    return projectMapper.mapToProjectResponse(validated.getProject(), null);
  }

}
