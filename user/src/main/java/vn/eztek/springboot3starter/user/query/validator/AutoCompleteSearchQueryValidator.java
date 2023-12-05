package vn.eztek.springboot3starter.user.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.role.repository.RoleRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.user.query.AutoCompleteSearchQuery;
import vn.eztek.springboot3starter.user.query.validated.AutoCompleteSearchQueryValidated;


@Component
@RequiredArgsConstructor
public class AutoCompleteSearchQueryValidator extends
    QueryValidation<AutoCompleteSearchQuery, AutoCompleteSearchQueryValidated> {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public AutoCompleteSearchQueryValidated validate(AutoCompleteSearchQuery query) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }
    var role = roleRepository.findByName(query.getRoleName())
        .orElseThrow(() -> new NotFoundException("role-not-found"));

    return AutoCompleteSearchQueryValidated.validatedOf(loggedInUser, role);
  }
}
