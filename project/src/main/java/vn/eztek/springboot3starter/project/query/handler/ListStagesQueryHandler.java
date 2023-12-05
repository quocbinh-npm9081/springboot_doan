package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.stage.repository.StageRepository;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.ListStagesQuery;
import vn.eztek.springboot3starter.project.query.validator.ListStagesQueryValidator;
import vn.eztek.springboot3starter.project.response.StageResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;

@Component
@RequiredArgsConstructor
public class ListStagesQueryHandler implements
    QueryHandler<ListStagesQuery, ListResponse<StageResponse>, ProjectAggregateId> {

  private final ListStagesQueryValidator validator;
  private final ProjectMapper projectMapper;
  private final StageRepository stageRepository;

  @Override
  public ListResponse<StageResponse> handle(ListStagesQuery query, ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var stages = stageRepository.findByProjectIdOrderByOrderNumberAsc(query.getProjectId());
    var res = stages.stream().map(projectMapper::mapToStageResponse).toList();

    // resulting
    return new ListResponse<>(res);
  }

}
