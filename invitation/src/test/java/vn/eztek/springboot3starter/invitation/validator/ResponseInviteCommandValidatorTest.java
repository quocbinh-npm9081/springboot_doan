package vn.eztek.springboot3starter.invitation.validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.command.validator.ResponseInviteCommandValidator;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ResponseInviteCommandValidatorTest {

  @Mock
  SecurityContext securityContext;
  @InjectMocks
  private ResponseInviteCommandValidator validator;
  @Mock
  private UserRepository userRepository;
  @Mock
  private InvitationRepository invitationRepository;
  @Mock
  private UserProjectRepository userProjectRepository;
  @Mock
  private Authentication authentication;

  private ResponseInviteCommand command;

  private User user;
  private User otherUser;
  private User userInActive;
  private Invitation invitation;
  private Invitation invitationWithProjectNull;
  private Invitation invitationWithProjectDeleted;
  private UserProject projectWithExitsMember;
  private UserProject projectWithNonExitsMember;

  @Before
  public void setup() {
    command = ResponseInviteCommand.commandOf(UUID.randomUUID(), true);

    user = new User("test@gmail.com", new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION))),
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)),
        UserStatus.ACTIVE);
    user.setId(UUID.randomUUID());

    otherUser = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.ACTIVE);
    otherUser.setId(UUID.randomUUID());

    userInActive = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.INACTIVE);

    User userWithRoleNotTalent = new User("test@gmail.com", new Role(RoleName.ADMINISTRATOR,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION))),
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)),
        UserStatus.ACTIVE);
    userWithRoleNotTalent.setId(UUID.randomUUID());

    invitation = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, new Project(),
        InvitationType.INVITE_TALENT_BY_MAIL);

    invitationWithProjectNull = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, null,
        InvitationType.INVITE_TALENT_BY_MAIL);

    Project projectDeleted = new Project(null, null, null, null, ZonedDateTime.now());
    invitationWithProjectDeleted = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, projectDeleted,
        InvitationType.INVITE_TALENT_BY_MAIL);

    projectWithExitsMember = new UserProject(UserProjectStatus.JOINED, user, new Project());
    projectWithNonExitsMember = new UserProject(UserProjectStatus.JOINED, otherUser, new Project());
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenInvalidKey() {
    // given
    // Create a mock authentication object
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.empty());

    // when
    validator.validate(command);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenUserNotExist() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));
    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.empty());

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserIsInActive() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(userInActive));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserIsNotMatchInvite() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(otherUser));

    // when
    validator.validate(command);
  }


  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectNull() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitationWithProjectNull));

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    // when
    validator.validate(command);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectIsDeleted() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitationWithProjectDeleted));

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    // when
    validator.validate(command);
  }


  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserIsInProject() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));
    when(userProjectRepository.findByProjectId(any())).thenReturn(List.of(projectWithExitsMember));

    // when
    validator.validate(command);
  }

  @Test
  public void validate_Result_ValidatedCommand_WhenAllPass() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(invitationRepository.findByIdAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));
    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(projectWithNonExitsMember));

    // when
    var result = validator.validate(command);

    // then
    assertNotNull(result.getInvitation());
  }
}
