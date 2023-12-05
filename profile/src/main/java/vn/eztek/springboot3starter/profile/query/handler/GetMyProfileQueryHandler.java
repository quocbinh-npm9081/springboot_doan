package vn.eztek.springboot3starter.profile.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.profile.mapper.ProfileMapper;
import vn.eztek.springboot3starter.profile.query.GetMyProfileQuery;
import vn.eztek.springboot3starter.profile.query.validator.GetMyProfileQueryValidator;
import vn.eztek.springboot3starter.profile.response.ProfileResponse;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class GetMyProfileQueryHandler
    implements QueryHandler<GetMyProfileQuery, ProfileResponse, ProfileAggregateId> {

  private final GetMyProfileQueryValidator validator;
  private final EventStoreRepository eventStoreRepository;
  private final ProfileMapper profileMapper;

  @Override
  public ProfileResponse handle(GetMyProfileQuery query, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.GET_MY_PROFILE,
        validated.getUser().getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
    var lastSignedInTime = eventStore.map(
        store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);

    // resulting
    return profileMapper.mapToProfileResponse(validated.getUser(), lastSignedInTime);

  }

}
