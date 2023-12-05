package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.SignInCommand;
import vn.eztek.springboot3starter.auth.command.event.UserSignedInEvent;
import vn.eztek.springboot3starter.auth.command.validator.SignInCommandValidator;
import vn.eztek.springboot3starter.auth.response.JwtResponse;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.security.jwt.JwtProvider;
import vn.eztek.springboot3starter.common.security.models.UserPrinciple;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class SignInCommandHandler
    implements CommandHandler<SignInCommand, JwtResponse, AuthAggregateId> {

  private final SignInCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final JwtProvider jwtProvider;

  @Override
  @Transactional
  public JwtResponse handle(SignInCommand command, AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    SecurityContextHolder.getContext().setAuthentication(validated.getAuthentication());
    var accessToken = jwtProvider.generateAccessToken(validated.getAuthentication());
    var refreshToken = jwtProvider.generateRefreshToken(validated.getAuthentication());

    var userDetails = (UserPrinciple) validated.getAuthentication().getPrincipal();
    var authorities = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    // event storing
    var event = UserSignedInEvent.eventOf(authAggregateId, userDetails.getId().toString(),
        userDetails.getUsername());
    eventStoreService.store(event);

    // resulting
    return new JwtResponse(accessToken, refreshToken, userDetails.getId(),
        userDetails.getFirstName(),
        userDetails.getLastName(), userDetails.getUsername(), userDetails.getRole(), authorities);
  }

}
