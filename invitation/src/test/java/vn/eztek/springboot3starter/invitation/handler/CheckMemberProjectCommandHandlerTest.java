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
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.invitation.command.CheckMemberProjectCommand;
import vn.eztek.springboot3starter.invitation.command.handler.CheckMemberProjectCommandHandler;
import vn.eztek.springboot3starter.invitation.command.validated.CheckMemberProjectCommandValidated;
import vn.eztek.springboot3starter.invitation.command.validator.CheckMemberProjectCommandValidator;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;

@RunWith(MockitoJUnitRunner.class)
public class CheckMemberProjectCommandHandlerTest {

  @InjectMocks
  private CheckMemberProjectCommandHandler handler;

  @Mock
  private CheckMemberProjectCommandValidator validator;

  @Mock
  InvitationEventService eventStoreService;
  private CheckMemberProjectCommand command;
  private CheckMemberProjectCommandValidated validated;

  @Before
  public void setUp() {
    command = CheckMemberProjectCommand.commandOf("2312");
    User user = new User();
    user.setId(UUID.randomUUID());
    Invitation invitation = new Invitation();
    invitation.setId(UUID.randomUUID());

    validated = CheckMemberProjectCommandValidated.validatedOf(user, invitation, new UserProject());
  }

  @Test
  public void handle_Result_CheckMemberProject_WhenSuccess() {

    // given
    when(validator.validate(command)).thenReturn(validated);

    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result.getIsMember()).isNotNull();
  }
}
