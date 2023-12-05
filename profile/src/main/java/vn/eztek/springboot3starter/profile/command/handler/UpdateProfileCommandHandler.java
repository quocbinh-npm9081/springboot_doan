package vn.eztek.springboot3starter.profile.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.event.repository.EventStoreRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.command.UpdateProfileCommand;
import vn.eztek.springboot3starter.profile.command.event.ProfileUpdatedEvent;
import vn.eztek.springboot3starter.profile.command.validator.UpdateProfileCommandValidator;
import vn.eztek.springboot3starter.profile.mapper.ProfileMapper;
import vn.eztek.springboot3starter.profile.response.ProfileResponse;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class UpdateProfileCommandHandler implements
    CommandHandler<UpdateProfileCommand, ProfileResponse, ProfileAggregateId> {

  private final UpdateProfileCommandValidator validator;
  private final UserRepository userRepository;
  private final ProfileEventStoreService eventStoreService;
  private final ProfileMapper profileMapper;
  private final EventStoreRepository eventStoreRepository;

  @Override
  public ProfileResponse handle(UpdateProfileCommand command, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = profileMapper.mapToUserBeforeUpdate(validated.getUser(), command);
    var updatedUser = userRepository.save(user);

    // event storing
    var eventStore = eventStoreRepository.findFirst1ByActionAndUserId(EventAction.SIGN_IN,
        updatedUser.getId().toString(), Sort.by(EventStore.CREATED_DATE).descending());
    var lastSignedInTime = eventStore.map(
        store -> DateUtils.longToZonedDateTime(store.getCreatedDate())).orElse(null);
    var event = ProfileUpdatedEvent.eventOf(entityId, updatedUser.getId().toString(),
        updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getPhoneNumber(),
        updatedUser.getGender());
    eventStoreService.store(event);

    // resulting
    return profileMapper.mapToUserResponse(user, lastSignedInTime);
  }
}
