package vn.eztek.springboot3starter.user.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.user.query.GetUserByIdQuery;
import vn.eztek.springboot3starter.user.query.validated.GetUserByIdQueryValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GetUserByIdQueryValidator extends
    QueryValidation<GetUserByIdQuery, GetUserByIdQueryValidated> {

  private final UserRepository userRepository;

  @Override
  public GetUserByIdQueryValidated validate(GetUserByIdQuery query) {
    var user = userRepository.findByIdAndDeletedAtNull(query.getId())
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    return GetUserByIdQueryValidated.validatedOf(user);
  }

}
