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
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.user.command.UpdateUserCommand;
import vn.eztek.springboot3starter.user.command.validated.UpdateUserCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateUserCommandValidator extends
    CommandValidation<UpdateUserCommand, UpdateUserCommandValidated> {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PrivilegeRepository privilegeRepository;

  @Override
  public UpdateUserCommandValidated validate(UpdateUserCommand command) {
    var user = userRepository.findByIdAndDeletedAtNull(command.getUserId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

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

    return UpdateUserCommandValidated.validatedOf(user, role, privileges);
  }

}
