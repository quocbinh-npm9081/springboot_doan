package vn.eztek.springboot3starter.task.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.GetTaskByIdQuery;
import vn.eztek.springboot3starter.task.query.validator.GetTaskByIdQueryValidator;
import vn.eztek.springboot3starter.task.response.TaskResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class GetTaskByIdQueryHandler implements
    QueryHandler<GetTaskByIdQuery, TaskResponse, TaskAggregateId> {

  private final GetTaskByIdQueryValidator validator;
  private final TaskMapper taskMapper;

  @Override
  public TaskResponse handle(GetTaskByIdQuery query, TaskAggregateId entityId) {

    var validated = validator.validate(query);

    return taskMapper.mapToTaskResponse(validated.getTask(), validated.getTask().getAttachments());
  }

}
