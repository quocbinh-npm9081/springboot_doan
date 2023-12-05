package vn.eztek.springboot3starter.invitation.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
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
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.validator.ConfirmInviteCommandValidator;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmInviteCommandValidatorTest {

  @Mock
  SecurityContext securityContext;
  @InjectMocks
  private ConfirmInviteCommandValidator validator;
  @Mock
  private UserRepository userRepository;
  @Mock
  private InvitationRepository invitationRepository;
  @Mock
  private UserProjectRepository userProjectRepository;
  @Mock
  private Authentication authentication;
  private ConfirmInviteCommand command;
  private User user;
  private Invitation invitation;
  private Invitation invitationWithProjectNull;
  private Invitation invitationWithProjectDeleted;
  private User userInActive;
  private User userWithRoleNotTalent;
  private UserProject userProjectContainUser;
  private UserProject userProjectNotContainUser;
  private UserProject userProjectContainUserWithStatusLeave;

  @Before
  public void setup() {
    command = ConfirmInviteCommand.commandOf("419af73b8f874c6587b36787fa4f6451");

    Project project = new Project();
    Project projectDeleted = new Project(null, null, null, null, ZonedDateTime.now());

    user = new User("test@gmail.com", new Role(RoleName.TALENT, null), null, UserStatus.ACTIVE);
    user.setId(UUID.randomUUID());

    User otherUser = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.ACTIVE);
    otherUser.setId(UUID.randomUUID());

    userInActive = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.INACTIVE);

    userWithRoleNotTalent = new User("test@gmail.com", new Role(RoleName.ADMINISTRATOR, null), null,
        UserStatus.ACTIVE);
    userWithRoleNotTalent.setId(UUID.randomUUID());

    invitation = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, project,
        InvitationType.INVITE_TALENT_BY_MAIL);

    invitationWithProjectNull = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, null,
        InvitationType.INVITE_TALENT_BY_MAIL);

    invitationWithProjectDeleted = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, projectDeleted,
        InvitationType.INVITE_TALENT_BY_MAIL);

    userProjectContainUser = new UserProject(UserProjectStatus.JOINED, user, new Project());
    userProjectContainUserWithStatusLeave = new UserProject(UserProjectStatus.LEAVED, user,
        new Project());
    userProjectNotContainUser = new UserProject(UserProjectStatus.JOINED, otherUser, new Project());
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenInvalidKey() {
    // given
    // Create a mock authentication object
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
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

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
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

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(userInActive));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserNotTalent() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitationWithProjectNull));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(userWithRoleNotTalent));

    // when
    validator.validate(command);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectNull() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitationWithProjectNull));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    // when
    validator.validate(command);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectDeleted() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitationWithProjectDeleted));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserIsMemberButLeave() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(userProjectContainUserWithStatusLeave));

    // when
    validator.validate(command);
  }

  @Test
  public void validate_Result_ValidatedCommand_WhenUserIsMember() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(userProjectRepository.findByProjectId(any())).thenReturn(List.of(userProjectContainUser));
    // when
    var result = validator.validate(command);

    // then
    assertEquals(true, result.getIsMember());
  }

  @Test
  public void validate_Result_ValidatedCommand_WhenUserIsNotMember() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(userProjectNotContainUser));
    // when
    var result = validator.validate(command);

    // then
    assertEquals(false, result.getIsMember());
  }

}
