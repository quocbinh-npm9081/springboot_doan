package vn.eztek.springboot3starter.project.command.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.project.command.CreateSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.event.SharedLinkCreatedEvent;
import vn.eztek.springboot3starter.project.command.validator.CreateSharedLinkCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.response.LinkShareResponse;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.util.GeneratorUtils;

@Component
@RequiredArgsConstructor
public class CreateSharedLinkCommandHandler implements
    CommandHandler<CreateSharedLinkCommand, LinkShareResponse, ProjectAggregateId> {

  private final CreateSharedLinkCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;
  private final InvitationRepository invitationRepository;

  @Override
  public LinkShareResponse handle(CreateSharedLinkCommand command, ProjectAggregateId entityId) {

    var validated = validator.validate(command);
    var key = validated.getInvitation();
    if (key != null) {
      key.setUsed(true);
      invitationRepository.save(key);
    }
    var keyRandom = GeneratorUtils.generateKey();
    key = projectMapper.mapToInvitation(null, validated.getUser(), validated.getProject(),
        keyRandom, InvitationType.INVITE_TALENT_BY_LINK);
    invitationRepository.save(key);

    var event = SharedLinkCreatedEvent.eventOf(entityId, validated.getUser().getId().toString(),
        validated.getProject().getId().toString());

    projectEventService.store(event);
    return projectMapper.mapToLinkShareResponse(keyRandom);
  }

}
