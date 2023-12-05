package vn.eztek.springboot3starter.publicAccess.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.publicAccess.mapper.PublicAccessMapper;
import vn.eztek.springboot3starter.publicAccess.query.GetProjectByKeyQuery;
import vn.eztek.springboot3starter.publicAccess.query.validator.GetProjectByKeyQueryValidator;
import vn.eztek.springboot3starter.publicAccess.response.ProjectResponse;
import vn.eztek.springboot3starter.publicAccess.vo.PublicAccessAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;

@Component
@RequiredArgsConstructor
public class GetProjectByKeyQueryHandler implements
    QueryHandler<GetProjectByKeyQuery, ProjectResponse, PublicAccessAggregateId> {

  private final GetProjectByKeyQueryValidator validator;
  private final PublicAccessMapper publicAccessMapper;

  @Override
  public ProjectResponse handle(GetProjectByKeyQuery query,
      PublicAccessAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // return response
    return publicAccessMapper.mapToProjectResponse(validated.getInvitation().getProject());
  }
}
