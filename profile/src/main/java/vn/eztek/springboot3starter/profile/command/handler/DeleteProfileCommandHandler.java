package vn.eztek.springboot3starter.profile.command.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.AccountDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.UserProjectMessage;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.profile.command.DeleteProfileCommand;
import vn.eztek.springboot3starter.profile.command.event.ProfileDeletedEvent;
import vn.eztek.springboot3starter.profile.command.validator.DeleteProfileCommandValidator;
import vn.eztek.springboot3starter.profile.mapper.ProfileMapper;
import vn.eztek.springboot3starter.profile.service.ProfileEventStoreService;
import vn.eztek.springboot3starter.profile.vo.ProfileAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class DeleteProfileCommandHandler implements
    CommandHandler<DeleteProfileCommand, EmptyCommandResult, ProfileAggregateId> {

  private final DeleteProfileCommandValidator validator;
  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final ProjectRepository projectRepository;
  private final ProfileEventStoreService eventStoreService;
  private final RedisMessagePublisher messagePublisher;
  private final ProfileMapper profileMapper;

  @Override
  public EmptyCommandResult handle(DeleteProfileCommand command, ProfileAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var user = validated.getUser();
    user.setDeletedAt(DateUtils.currentZonedDateTime());
    userRepository.save(user);

    List<Project> projects = projectRepository.findByOwnerId(user.getId());
    projects.forEach(project -> {
      project.setDeletedAt(DateUtils.currentZonedDateTime());
    });
    projectRepository.saveAll(projects);

    // event storing
    var event = ProfileDeletedEvent.eventOf(entityId, user.getId().toString(), user.getFirstName(),
        user.getLastName(), user.getPhoneNumber(), user.getGender());
    eventStoreService.store(event);

    // send event to queue

    List<UserProjectMessage> userProjects = userProjectRepository.findByProjectOwnerIdAndUserIdNot(
            validated.getUser().getId(), validated.getUser().getId()).stream()
        .map(profileMapper::mapToUserProjectMessage).toList();

    messagePublisher.publish(AccountDeleteMessage.create(user.getId().toString(),
        user.getFirstName() + " " + user.getLastName(), user.getUsername(), userProjects));

    // resulting
    return EmptyCommandResult.empty();
  }
}
