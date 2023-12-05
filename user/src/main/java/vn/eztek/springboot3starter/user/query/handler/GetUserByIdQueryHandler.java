package vn.eztek.springboot3starter.user.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;
import vn.eztek.springboot3starter.user.mapper.UserMapper;
import vn.eztek.springboot3starter.user.query.GetUserByIdQuery;
import vn.eztek.springboot3starter.user.query.validator.GetUserByIdQueryValidator;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.vo.UserAggregateId;

@Component
@RequiredArgsConstructor
public class GetUserByIdQueryHandler
    implements QueryHandler<GetUserByIdQuery, UserResponse, UserAggregateId> {

  private final GetUserByIdQueryValidator validator;
  private final EventStoreRepository eventStoreRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponse handle(GetUserByIdQuery query, UserAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.SIGN_IN,
        validated.getUser().getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
    var lastSignedInTime = eventStore.map(
        store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);

    // resulting
    return userMapper.mapToUserResponse(validated.getUser(), lastSignedInTime);
  }
}
