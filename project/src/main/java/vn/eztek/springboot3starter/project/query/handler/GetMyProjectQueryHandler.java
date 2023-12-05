package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.GetMyProjectQuery;
import vn.eztek.springboot3starter.project.query.validator.GetMyProjectQueryValidator;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;

@Component
@RequiredArgsConstructor
public class GetMyProjectQueryHandler
    implements QueryHandler<GetMyProjectQuery, ListResponse<ProjectResponse>, ProjectAggregateId> {

  private final GetMyProjectQueryValidator validator;
  private final ProjectMapper projectMapper;

  @Override
  public ListResponse<ProjectResponse> handle(GetMyProjectQuery query,
      ProjectAggregateId entityId) {

    var validated = validator.validate(query);

    var projects = validated.getProjects().stream().map(
        userProject -> projectMapper.mapToProjectResponse(
            userProject.getProject(),
            userProject.getStatus())
    ).toList();

    return new ListResponse<>(projects);
  }

}
