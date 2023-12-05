package vn.eztek.springboot3starter.auth.command.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.RefreshTokenCommand;
import vn.eztek.springboot3starter.auth.command.event.TokenRefreshEvent;
import vn.eztek.springboot3starter.auth.command.validator.RefreshTokenCommandValidator;
import vn.eztek.springboot3starter.auth.response.JwtResponse;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.common.security.jwt.JwtProvider;
import vn.eztek.springboot3starter.common.security.models.UserPrinciple;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class RefreshTokenCommandHandler
    implements CommandHandler<RefreshTokenCommand, JwtResponse, AuthAggregateId> {

  private final RefreshTokenCommandValidator validator;
  private final AuthEventStoreService eventStoreService;
  private final JwtProvider jwtProvider;

  @Override
  @Transactional
  public JwtResponse handle(RefreshTokenCommand command, AuthAggregateId authAggregateId) {
    // validating
    var validated = validator.validate(command);

    // handling
    var accessToken = jwtProvider.generateAccessToken(validated.getAuthentication());
    var refreshToken = jwtProvider.generateRefreshToken(validated.getAuthentication());
    var userDetails = (UserPrinciple) validated.getAuthentication().getPrincipal();

    var authorities = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    // event storing
    var event = TokenRefreshEvent.eventOf(authAggregateId, userDetails.getId().toString());
    eventStoreService.store(event);

    // resulting
    return new JwtResponse(accessToken, refreshToken, userDetails.getId(),
        userDetails.getFirstName(),
        userDetails.getLastName(), userDetails.getUsername(), userDetails.getRole(), authorities);
  }

}
