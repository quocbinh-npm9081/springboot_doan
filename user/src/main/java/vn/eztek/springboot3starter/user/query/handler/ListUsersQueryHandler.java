package vn.eztek.springboot3starter.user.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.PageResponse;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.query.ListUsersQuery;
import vn.eztek.springboot3starter.user.query.validator.ListUsersQueryValidator;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.specification.UserSpecifications;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class ListUsersQueryHandler
    implements QueryHandler<ListUsersQuery, PageResponse<UserResponse>, UserAggregateId> {

  private final ListUsersQueryValidator validator;
  private final UserRepository userRepository;
  private final EventStoreRepository eventStoreRepository;
  private final UserMapper userMapper;

  @Override
  public PageResponse<UserResponse> handle(ListUsersQuery query, UserAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var specification = UserSpecifications.getFilter(query.getCriteria());
    var page = userRepository.findAll(specification, query.getPageable());

    var userResponses = page.getContent().stream().map(user -> {
      var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.SIGN_IN,
          user.getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
      var lastSignedInTime = eventStore.map(
          store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);
      return userMapper.mapToUserResponse(user, lastSignedInTime);
    }).toList();

    // resulting
    return new PageResponse<>(userResponses, page.getPageable(), page.getTotalElements());
  }
}
