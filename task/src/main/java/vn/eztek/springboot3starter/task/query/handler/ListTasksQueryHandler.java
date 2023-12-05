package vn.eztek.springboot3starter.task.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.ListTasksQuery;
import vn.eztek.springboot3starter.task.query.validator.ListTasksQueryValidator;
import vn.eztek.springboot3starter.task.response.SimplifyTaskResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class ListTasksQueryHandler implements
    QueryHandler<ListTasksQuery, ListResponse<SimplifyTaskResponse>, TaskAggregateId> {

  private final ListTasksQueryValidator validator;
  private final TaskMapper taskMapper;
  private final TaskRepository taskRepository;

  @Override
  public ListResponse<SimplifyTaskResponse> handle(ListTasksQuery query, TaskAggregateId entityId) {
    // validating
    validator.validate(query);

    // handling
    var tasks = taskRepository.findByProjectId(query.getProjectId());
    var result = tasks.stream().map(task -> taskMapper.mapToSimplifyTaskResponse(task,
        task.getDescription() != null && !task.getDescription().isBlank())).toList();

    // resulting
    return new ListResponse<>(result);
  }

}
