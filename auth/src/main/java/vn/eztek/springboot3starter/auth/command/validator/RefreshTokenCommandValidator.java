package vn.eztek.springboot3starter.auth.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.RefreshTokenCommand;
import vn.eztek.springboot3starter.auth.command.validated.RefreshTokenCommandValidated;
import vn.eztek.springboot3starter.common.security.jwt.JwtProvider;
import vn.eztek.springboot3starter.common.security.models.TokenType;
import vn.eztek.springboot3starter.common.security.services.UserDetailsServiceImpl;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class RefreshTokenCommandValidator
    extends CommandValidation<RefreshTokenCommand, RefreshTokenCommandValidated> {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtProvider tokenProvider;
  private final UserRepository userRepository;

  @Override
  public RefreshTokenCommandValidated validate(RefreshTokenCommand command) {
    tokenProvider.validateJwtToken(command.getRefreshToken());
    var token = tokenProvider.getTokenType(command.getRefreshToken());

    if (token == null || !token.equals(TokenType.REFRESH_TOKEN)) {
      throw new BadRequestException("invalid-token");
    }

    var username = tokenProvider.getUserNameFromJwtToken(command.getRefreshToken());
    var userDetails = userDetailsService.loadUserByUsername(username);

    if (!userRepository.existsByUsernameAndStatusAndDeletedAtNull(username, UserStatus.ACTIVE)) {
      throw new BadRequestException("user-is-not-active");
    }

    var authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource());

    return RefreshTokenCommandValidated.validatedOf(authentication);

  }

}
