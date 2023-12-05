package vn.eztek.springboot3starter.task.query.handler.checklist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.checkList.repository.CheckListRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.checklist.GetCheckListByTaskIdQuery;
import vn.eztek.springboot3starter.task.query.validator.checklist.GetCheckListByTaskIdQueryValidator;
import vn.eztek.springboot3starter.task.response.CheckListResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class GetCheckListByTaskIdQueryHandler implements
    QueryHandler<GetCheckListByTaskIdQuery, ListResponse<CheckListResponse>, TaskAggregateId> {

  private final GetCheckListByTaskIdQueryValidator validator;
  private final TaskMapper taskMapper;
  private final CheckListRepository checkListRepository;

  @Override
  public ListResponse<CheckListResponse> handle(GetCheckListByTaskIdQuery query,
      TaskAggregateId entityId) {

    validator.validate(query);

    var checkLists = checkListRepository.findByTaskId(query.getTaskId());

    var res = checkLists.stream()
        .map(taskMapper::mapToCheckListResponse)
        .toList();
    return new ListResponse<>(res);
  }

}
