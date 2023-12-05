package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.ListProjectQuery;
import vn.eztek.springboot3starter.project.query.validator.ListProjectQueryValidator;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.specification.ProjectSpecifications;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.PageResponse;

@Component
@RequiredArgsConstructor
public class ListProjectQueryHandler
    implements QueryHandler<ListProjectQuery, PageResponse<ProjectResponse>, ProjectAggregateId> {

  private final ListProjectQueryValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectRepository projectRepository;

  @Override
  public PageResponse<ProjectResponse> handle(ListProjectQuery query, ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var specification = ProjectSpecifications.getFilter(query.getCriteria());
    var page = projectRepository.findAll(specification, query.getPageable());

    var projectResponses = page.getContent().stream().map(x ->
        projectMapper.mapToProjectResponse(x, null)).toList();

    // resulting
    return new PageResponse<>(projectResponses, page.getPageable(), page.getTotalElements());
  }

}
