package vn.eztek.springboot3starter.task.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.comment.repository.CommentRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.mapper.TaskMapper;
import vn.eztek.springboot3starter.task.query.GetChildrenCommentQuery;
import vn.eztek.springboot3starter.task.query.validator.GetChildrenCommentQueryValidator;
import vn.eztek.springboot3starter.task.response.CommentResponse;
import vn.eztek.springboot3starter.task.vo.TaskAggregateId;

@Component
@RequiredArgsConstructor
public class GetChildrenCommentQueryHandler implements
    QueryHandler<GetChildrenCommentQuery, ListResponse<CommentResponse>, TaskAggregateId> {

  private final GetChildrenCommentQueryValidator validator;
  private final TaskMapper taskMapper;
  private final CommentRepository commentRepository;

  @Override
  public ListResponse<CommentResponse> handle(GetChildrenCommentQuery query,
      TaskAggregateId entityId) {

    validator.validate(query);

    var comments = commentRepository.findByParentCommentId(query.getParentCommentId());

    var res = comments.stream()
        .map(taskMapper::mapToCommentResponse)
        .toList();

    return new ListResponse<>(res);
  }

}
