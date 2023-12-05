package vn.eztek.springboot3starter.profile.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.profile.query.GetMyProfileQuery;
import vn.eztek.springboot3starter.profile.query.validated.GetMyProfileQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetMyProfileQueryValidator extends
    QueryValidation<GetMyProfileQuery, GetMyProfileQueryValidated> {

  private final UserRepository userRepository;

  @Override
  public GetMyProfileQueryValidated validate(GetMyProfileQuery query) {
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(query.getUsername())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    return GetMyProfileQueryValidated.validatedOf(user);
  }

}
