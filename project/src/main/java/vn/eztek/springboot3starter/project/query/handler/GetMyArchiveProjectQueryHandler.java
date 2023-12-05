package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.GetMyArchiveProjectQuery;
import vn.eztek.springboot3starter.project.query.validator.GetMyArchiveProjectQueryValidator;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;

@Component
@RequiredArgsConstructor
public class GetMyArchiveProjectQueryHandler implements
    QueryHandler<GetMyArchiveProjectQuery, ListResponse<ProjectResponse>, ProjectAggregateId> {

  private final GetMyArchiveProjectQueryValidator validator;
  private final ProjectMapper projectMapper;

  @Override
  public ListResponse<ProjectResponse> handle(GetMyArchiveProjectQuery query,
      ProjectAggregateId entityId) {

    var validated = validator.validate(query);

    var projects = validated.getProjects().stream()
        .map(project -> projectMapper.mapToProjectResponse(project, null)).toList();

    return new ListResponse<>(projects);
  }

}
