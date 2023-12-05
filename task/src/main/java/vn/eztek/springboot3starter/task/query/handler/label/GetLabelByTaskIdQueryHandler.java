package vn.eztek.springboot3starter.task.query.handler.label;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.label.repository.LabelRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdQuery;
import vn.eztek.springboot3starter.task.query.validator.label.GetLabelByTaskIdQueryValidator;
import vn.eztek.springboot3starter.task.response.LabelResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class GetLabelByTaskIdQueryHandler implements
        QueryHandler<GetLabelByTaskIdQuery, ListResponse<LabelResponse>, TaskAggregateId> {
  private final GetLabelByTaskIdQueryValidator validator;
  private final TaskMapper taskMapper;
  private final LabelRepository labelRepository;

  @Override
  public ListResponse<LabelResponse> handle(GetLabelByTaskIdQuery query, TaskAggregateId entityId) {
    validator.validate(query);

    var labels = labelRepository.findByTaskId(query.getTaskId());

    var res = labels.stream().map(taskMapper::mapToLabelResponse)
            .toList();
    return new ListResponse<>(res);
  }
}
