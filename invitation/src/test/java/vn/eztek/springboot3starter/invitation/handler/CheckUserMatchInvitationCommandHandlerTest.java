package vn.eztek.springboot3starter.invitation.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.invitation.command.CheckUserMatchInviteCommand;
import vn.eztek.springboot3starter.invitation.command.handler.CheckUserMatchInviteCommandHandler;
import vn.eztek.springboot3starter.invitation.command.validated.CheckUserMatchInviteCommandValidated;
import vn.eztek.springboot3starter.invitation.command.validator.CheckUserMatchInviteCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.response.CheckUserMatchInviteResponse;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;

@RunWith(MockitoJUnitRunner.class)
public class CheckUserMatchInvitationCommandHandlerTest {

  @InjectMocks
  private CheckUserMatchInviteCommandHandler handler;
  @Mock
  private CheckUserMatchInviteCommandValidator validator;
  @Mock
  private InvitationMapper invitationMapper;
  @Mock
  InvitationEventService eventStoreService;
  private CheckUserMatchInviteCommand command;
  private CheckUserMatchInviteCommandValidated validated;

  @Before
  public void setUp() {
    command = CheckUserMatchInviteCommand.commandOf("2312");
    User user = new User();
    user.setId(UUID.randomUUID());
    Invitation invitation = new Invitation();
    invitation.setUser(user);
    invitation.setId(UUID.randomUUID());

    validated = CheckUserMatchInviteCommandValidated.validatedOf(user, invitation);
  }

  @Test
  public void handle_Result_CheckUserMatchInvitation_WhenSuccess() {

    // given
    when(validator.validate(command)).thenReturn(validated);
    when(invitationMapper.mapToCheckResponse(any())).thenReturn(new CheckUserMatchInviteResponse(true));

    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result.getIsMatch()).isTrue();
  }
}
