package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.ListProjectMembersQuery;
import vn.eztek.springboot3starter.project.query.validator.ListProjectMembersQueryValidator;
import vn.eztek.springboot3starter.project.response.ProjectMemberResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;

@Component
@RequiredArgsConstructor
public class ListProjectMembersQueryHandler implements
    QueryHandler<ListProjectMembersQuery, ListResponse<ProjectMemberResponse>, ProjectAggregateId> {

  private final ListProjectMembersQueryValidator validator;
  private final ProjectMapper projectMapper;

  @Override
  public ListResponse<ProjectMemberResponse> handle(ListProjectMembersQuery query,
      ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var result = validated.getUserProjects().stream().map(projectMapper::mapToProjectMember)
        .toList();

    // resulting
    return new ListResponse<>(result);
  }

}
