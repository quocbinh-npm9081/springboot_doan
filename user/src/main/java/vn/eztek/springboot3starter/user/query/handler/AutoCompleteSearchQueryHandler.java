package vn.eztek.springboot3starter.user.query.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.query.AutoCompleteSearchQuery;
import vn.eztek.springboot3starter.user.query.validator.AutoCompleteSearchQueryValidator;
import vn.eztek.springboot3starter.user.response.AutoCompleteSearchResponse;
import vn.eztek.springboot3starter.user.specification.UserSpecifications;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class AutoCompleteSearchQueryHandler implements
    QueryHandler<AutoCompleteSearchQuery, ListResponse<AutoCompleteSearchResponse>, UserAggregateId> {

  private final AutoCompleteSearchQueryValidator validator;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final int DEFAULT_PAGE_SIZE = 5;

  @Override
  public ListResponse<AutoCompleteSearchResponse> handle(AutoCompleteSearchQuery query,
      UserAggregateId entityId) {
    // validating
    validator.validate(query);

    // handling
    if (query.getName() == null || query.getName().isEmpty()) {
      return new ListResponse<>(List.of());
    } else {
      var specification = UserSpecifications.getFilter(query);
      var users = userRepository.findAll(specification, Pageable.ofSize(DEFAULT_PAGE_SIZE));
      var res = users.stream().map(userMapper::mapToAutoCompleteSearch).toList();
      return new ListResponse<>(res);
    }
  }
}
