package vn.eztek.springboot3starter.invitation.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.role.repository.RoleRepository;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.validator.InviteTalentCommandValidator;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class InviteTalentCommandValidatorTest {

  @Mock
  SecurityContext securityContext;
  @InjectMocks
  private InviteTalentCommandValidator validator;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserProjectRepository userProjectRepository;
  @Mock
  private Authentication authentication;
  @Mock
  private RoleRepository roleRepository;
  private InviteTalentCommand command;
  private InviteTalentCommand commandWithListEmailNull;
  private InviteTalentCommand commandWithListEmailEmpty;
  private InviteTalentCommand commandWithEmailBlank;
  private User user;
  private User userInActive;
  private User userWithRoleNotTalent;
  private User otherUser;
  private UserProject projectWithExitsMember;

  @Before
  public void setup() {
    command = InviteTalentCommand.commandOf(UUID.randomUUID(),
        List.of("test1@gmail.com", "test2@gmail.com"));
    commandWithListEmailNull = InviteTalentCommand.commandOf(UUID.randomUUID(), null);
    commandWithListEmailEmpty = InviteTalentCommand.commandOf(UUID.randomUUID(), List.of());
    commandWithEmailBlank = InviteTalentCommand.commandOf(UUID.randomUUID(), List.of(""));

    user = new User("test@gmail.com", new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION))),
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)),
        UserStatus.ACTIVE);
    user.setId(UUID.randomUUID());

    projectWithExitsMember = new UserProject(UserProjectStatus.JOINED, user, new Project());

    otherUser = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.ACTIVE);
    otherUser.setId(UUID.randomUUID());

    userInActive = new User("test@gmail.com", new Role(RoleName.TALENT, null), null,
        UserStatus.INACTIVE);

    userWithRoleNotTalent = new User("test@gmail.com", new Role(RoleName.ADMINISTRATOR,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION))),
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)),
        UserStatus.ACTIVE);
    userWithRoleNotTalent.setId(UUID.randomUUID());

  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenListEmailIsNull() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    // when
    validator.validate(commandWithListEmailNull);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenListEmailIsEmpty() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    // when
    validator.validate(commandWithListEmailEmpty);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenRoleNotFound() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.empty());
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

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT, null)));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.empty());

    // when
    validator.validate(command);
  }

  @Test(expected = NotFoundException.class)
  public void validate_Throw_NotFoundException_WhenProjectNotFound() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));

    when(userProjectRepository.findByProjectId(any())).thenReturn(List.of());

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

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(userInActive));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, user, new Project())));
    // when
    validator.validate(command);
  }

  @Test(expected = AccessDeniedException.class)
  public void validate_Throw_AccessDeniedException_WhenUserNotInProject() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));
    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, otherUser, new Project())));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenListEmailContainEmailBlank() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user));
    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, user, new Project())));

    // when
    validator.validate(commandWithEmailBlank);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenExistTalentNotActive() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user), Optional.of(userInActive));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, user, new Project())));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserExistInProject() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user), Optional.of(user));

    when(userProjectRepository.findByProjectId(any())).thenReturn(List.of(projectWithExitsMember));

    // when
    validator.validate(command);
  }

  @Test(expected = BadRequestException.class)
  public void validate_Throw_BadRequestException_WhenUserInviteIsNotTalent() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user)).thenReturn(Optional.of(userWithRoleNotTalent));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, user, new Project())));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user), Optional.of(userWithRoleNotTalent));

    // when
    validator.validate(command);
  }

  @Test
  public void validate_Result_ValidatedCommand_WhenAllPass() {
    // given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(
        "test@gmail.com");

    when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(RoleName.TALENT,
        Set.of(new Privilege(UUID.randomUUID(), PrivilegeName.CREATE_INVITATION)))));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user)).thenReturn(Optional.of(userWithRoleNotTalent));

    when(userProjectRepository.findByProjectId(any())).thenReturn(
        List.of(new UserProject(UserProjectStatus.JOINED, user, new Project())));

    when(userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(any())).thenReturn(
        Optional.of(user), Optional.of(otherUser), Optional.empty());

    // when
    var result = validator.validate(command);

    // then
    assertEquals(result.getUsers().size(), 2);
  }
}
