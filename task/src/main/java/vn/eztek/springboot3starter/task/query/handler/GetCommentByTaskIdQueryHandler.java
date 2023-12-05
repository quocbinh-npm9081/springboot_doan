package vn.eztek.springboot3starter.task.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.GetCommentByTaskIdQuery;
import vn.eztek.springboot3starter.task.query.validator.GetCommentByTaskIdQueryValidator;
import vn.eztek.springboot3starter.task.response.CommentResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class GetCommentByTaskIdQueryHandler implements
    QueryHandler<GetCommentByTaskIdQuery, ListResponse<CommentResponse>, TaskAggregateId> {

  private final GetCommentByTaskIdQueryValidator validator;
  private final TaskMapper taskMapper;
  private final CommentRepository commentRepository;

  @Override
  public ListResponse<CommentResponse> handle(GetCommentByTaskIdQuery query,
      TaskAggregateId entityId) {

    validator.validate(query);

    var comments = commentRepository.findAllByTaskIdAndParentCommentIdNull(query.getTaskId());

    var res = comments.stream()
        .map(taskMapper::mapToCommentResponse)
        .toList();
    return new ListResponse<>(res);
  }

}
