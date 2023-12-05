package vn.eztek.springboot3starter.user.command.validator;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.repository.PrivilegeRepository;
import vn.eztek.springboot3starter.domain.role.repository.RoleRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.ConflictException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.user.command.CreateUserCommand;
import vn.eztek.springboot3starter.user.command.validated.CreateUserCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateUserCommandValidator
    extends CommandValidation<CreateUserCommand, CreateUserCommandValidated> {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PrivilegeRepository privilegeRepository;

  @Override
  public CreateUserCommandValidated validate(CreateUserCommand command) {
    if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtNull(command.getUsername())) {
      throw new ConflictException("username-already-exists");
    }

    var role = roleRepository.findByName(command.getRole())
        .orElseThrow(() -> new NotFoundException("role-not-found"));

    Set<Privilege> privileges = new HashSet<>();
    if (!CollectionUtils.isEmpty(command.getPrivileges())) {
      command.getPrivileges().forEach(privilegeName -> {
        if (role.getPrivileges().stream().noneMatch(p -> p.getName().equals(privilegeName))) {
          throw new BadRequestException(command.getRole() + "-does-not-contain-" + privilegeName);
        }
        var privilege = privilegeRepository.findByName(privilegeName)
            .orElseThrow(() -> new NotFoundException("privilege-not-found"));
        privileges.add(privilege);
      });
    }

    return CreateUserCommandValidated.validatedOf(role, privileges);
  }

}
