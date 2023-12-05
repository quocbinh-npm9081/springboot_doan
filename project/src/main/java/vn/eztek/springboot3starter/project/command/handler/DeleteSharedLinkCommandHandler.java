package vn.eztek.springboot3starter.project.command.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.project.command.DeleteSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.event.SharedLinkDeletedEvent;
import vn.eztek.springboot3starter.project.command.validator.DeleteSharedLinkCommandValidator;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@Component
@RequiredArgsConstructor
public class DeleteSharedLinkCommandHandler implements
    CommandHandler<DeleteSharedLinkCommand, EmptyCommandResult, ProjectAggregateId> {

  private final DeleteSharedLinkCommandValidator validator;
  private final ProjectEventService projectEventService;
  private final InvitationRepository invitationRepository;

  @Override
  public EmptyCommandResult handle(DeleteSharedLinkCommand command, ProjectAggregateId entityId) {

    // validating
    var validated = validator.validate(command);

    // handling
    var key = validated.getInvitation();
    key.setUsed(true);
    invitationRepository.save(key);

    // event
    var event = SharedLinkDeletedEvent.eventOf(entityId, validated.getUser().getId().toString(),
        key.getId().toString());
    projectEventService.store(event);

    // result
    return EmptyCommandResult.empty();
  }

}
