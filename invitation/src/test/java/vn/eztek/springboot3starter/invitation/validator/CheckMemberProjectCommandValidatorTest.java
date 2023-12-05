package vn.eztek.springboot3starter.invitation.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;
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
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.CheckMemberProjectCommand;
import vn.eztek.springboot3starter.invitation.command.validator.CheckMemberProjectCommandValidator;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class CheckMemberProjectCommandValidatorTest {

  @InjectMocks
  private CheckMemberProjectCommandValidator validator;
  @Mock
  private UserRepository userRepository;
  @Mock
  private InvitationRepository invitationRepository;
  @Mock
  private UserProjectRepository userProjectRepository;
  @Mock
  private Authentication authentication;
  @Mock
  SecurityContext securityContext;
  private CheckMemberProjectCommand command;
  private User user;
  private Invitation invitation;
  private Invitation invitationWithProjectNull;
  private Invitation invitationWithProjectDeleted;
  private User userInActive;
  private UserProject userProjectWithStatusLeaved;

  @Before
  public void setup() {
    command = CheckMemberProjectCommand.commandOf("419af73b8f874c6587b36787fa4f6451");

    Project project = new Project();
    Project projectDeleted = new Project(null, null, null, null, ZonedDateTime.now());

    user = new User("test@gmail.com", null, null, UserStatus.ACTIVE);
    userInActive = new User("test@gmail.com", null, null, UserStatus.INACTIVE);

    invitation = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, project,
        InvitationType.INVITE_TALENT_BY_LINK);

    invitationWithProjectNull = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, null,
        InvitationType.INVITE_TALENT_BY_LINK);

    invitationWithProjectDeleted = new Invitation("80458de1d1ef4ce794f5a0176007b801",
        DateUtils.currentZonedDateTime().minusHours(-1), false, user, user, projectDeleted,
        InvitationType.INVITE_TALENT_BY_LINK);
    userProjectWithStatusLeaved = new UserProject(UserProjectStatus.LEAVED, null, null);
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

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectNull() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

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
  public void validate_Throw_BadRequestException_WhenUserProjectLeave() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));
    when(userProjectRepository.findByProjectIdAndUserId(any(), any())).thenReturn(
        Optional.of(userProjectWithStatusLeaved));

    // when
    validator.validate(command);
  }

  @Test
  public void validate_Result_ValidatedCommand_WhenPassAll() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(invitationRepository.findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(any(), any(),
        any())).thenReturn(Optional.of(invitation));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    // when
    var result = validator.validate(command);

    // then
    assertEquals("test@gmail.com", result.getUser().getUsername());
  }
}
