package vn.eztek.springboot3starter.invitation.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.property.SendGridProperties.Client;
import vn.eztek.springboot3starter.common.property.SendGridProperties.DynamicTemplateId;
import vn.eztek.springboot3starter.common.property.SendGridProperties.Path;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.handler.InviteTalentCommandHandler;
import vn.eztek.springboot3starter.invitation.command.validated.InviteTalentCommandValidated;
import vn.eztek.springboot3starter.invitation.command.validator.InviteTalentCommandValidator;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.service.InvitationEventService;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;

@RunWith(MockitoJUnitRunner.class)
public class InviteTalentCommandHandlerTest {

  @InjectMocks
  private InviteTalentCommandHandler handler;
  @Mock
  private InviteTalentCommandValidator validator;
  @Mock
  InvitationEventService eventStoreService;
  @Mock
  UserProjectRepository userProjectRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  InvitationRepository invitationRepository;
  @Mock
  RedisMessagePublisher redisMessagePublisher;
  @Mock
  SendGridProperties sendGridProperties;
  @Mock
  InvitationMapper invitationMapper;
  private InviteTalentCommand command;
  private User user;

  @Before
  public void setUp() {
    UUID projectId = UUID.randomUUID();
    command = InviteTalentCommand.commandOf(projectId, List.of("test@gmail.com"));

    user = new User();
    Project project = new Project();
    project.setId(UUID.randomUUID());
    user.setId(UUID.randomUUID());
    Invitation invitation = new Invitation();
    invitation.setId(UUID.randomUUID());
    Map<String, User> map = new HashMap<>();

    map.put("123", null);
    map.put("1231231", user);
    Set<Privilege> privileges = Set.of(
        new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION));
    Role role = new Role(RoleName.TALENT, privileges);

    InviteTalentCommandValidated validated = InviteTalentCommandValidated.validatedOf(map, user,
        project, role, privileges);

    when(validator.validate(command)).thenReturn(validated);
    when(sendGridProperties.getClient()).thenReturn(new Client());
    when(sendGridProperties.getPath()).thenReturn(new Path());
    when(sendGridProperties.getDynamicTemplateId()).thenReturn(new DynamicTemplateId());
  }

  @Test
  public void handle_Result_InviteTalentWithEmulatorFalse_WhenSuccess() {

    // given
    ReflectionTestUtils.setField(handler, "enableKeyEmulator", false);
    when(userRepository.save(any())).thenReturn(user);
    when(
        invitationRepository.findByUserIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(any(),
            any(), any(), any())).thenReturn(Optional.of(new Invitation()));
    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isNotNull();
  }

  @Test
  public void handle_Result_InviteTalentWithEmulatorTrue_WhenSuccess() {

    // given
    ReflectionTestUtils.setField(handler, "enableKeyEmulator", true);
    when(userRepository.save(any())).thenReturn(user);
    // when
    var result = handler.handle(command, new InvitationAggregateId());

    // then
    assertThat(result).isNotNull();
  }
}
