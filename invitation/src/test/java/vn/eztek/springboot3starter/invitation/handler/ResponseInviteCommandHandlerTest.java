package vn.eztek.springboot3starter.invitation.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.command.handler.ResponseInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.command.validated.ResponseInviteCommandValidated;
import vn.eztek.springboot3starter.invitation.command.validator.ResponseInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.EmptyCommandResult;

@RunWith(MockitoJUnitRunner.class)
public class ResponseInviteCommandHandlerTest {

  @InjectMocks
  private ResponseInviteCommandHandler handler;
  @Mock
  private ResponseInviteCommandValidator validator;
  @Mock
  InvitationEventService eventStoreService;
  @Mock
  UserProjectRepository userProjectRepository;
  @Mock
  InvitationRepository invitationRepository;
  @Mock
  InvitationMapper invitationMapper;
  private ResponseInviteCommand command;
  private ResponseInviteCommandValidated validated;

  @Before
  public void setUp() {
    User  user = new User();
    Project project = new Project();
    project.setId(UUID.randomUUID());
    user.setId(UUID.randomUUID());
    Invitation invitation = new Invitation();
    invitation.setId(UUID.randomUUID());

    validated = ResponseInviteCommandValidated.validatedOf(invitation, user, project);

  }

  @Test
  public void handle_Result_ResponseAcceptInvite_WhenSuccess() {

    // given
    command = ResponseInviteCommand.commandOf(UUID.randomUUID(), true);
    when(validator.validate(command)).thenReturn(validated);

    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isNotNull();
  }

  @Test
  public void handle_Result_ResponseRejectInvite_WhenSuccess() {

    // given
    command = ResponseInviteCommand.commandOf(UUID.randomUUID(), false);
    when(validator.validate(command)).thenReturn(validated);

    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isEqualTo(EmptyCommandResult.empty());
  }
}
