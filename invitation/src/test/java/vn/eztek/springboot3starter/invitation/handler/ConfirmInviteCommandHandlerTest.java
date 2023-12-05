package vn.eztek.springboot3starter.invitation.handler;

import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.handler.ConfirmInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.command.validated.ConfirmInviteCommandValidated;
import vn.eztek.springboot3starter.invitation.command.validator.ConfirmInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmInviteCommandHandlerTest {

  @InjectMocks
  private ConfirmInviteCommandHandler handler;
  @Mock
  private ConfirmInviteCommandValidator validator;
  @Mock
  InvitationEventService eventStoreService;
  @Mock
  UserProjectRepository userProjectRepository;
  @Mock
  private InvitationMapper invitationMapper;
  private ConfirmInviteCommand command;
  private ConfirmInviteCommandValidated validatedIsMember;
  private ConfirmInviteCommandValidated validatedNotIsMember;

  @Before
  public void setUp() {
    command = ConfirmInviteCommand.commandOf("2312");
    User user = new User();
    Project project = new Project();
    project.setId(UUID.randomUUID());
    user.setId(UUID.randomUUID());
    Invitation invitation = new Invitation();
    invitation.setId(UUID.randomUUID());

    validatedIsMember = ConfirmInviteCommandValidated.validatedOf(invitation, user, project,
        true);
    validatedNotIsMember = ConfirmInviteCommandValidated.validatedOf(invitation, user,
        project, false);

  }

  @Test
  public void handle_Result_ConfirmInviteWithUserNotIsMember_WhenSuccess() {

    // given
    when(validator.validate(command)).thenReturn(validatedNotIsMember);
    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isNotNull();
  }

  @Test
  public void handle_Result_ConfirmInviteWithUserIsMember_WhenSuccess() {

    // given
    when(validator.validate(command)).thenReturn(validatedIsMember);
    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isNotNull();
  }
}
