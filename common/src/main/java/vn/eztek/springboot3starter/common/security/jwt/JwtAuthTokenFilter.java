package vn.eztek.springboot3starter.common.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.eztek.springboot3starter.common.exception.UnauthorizedException;
import vn.eztek.springboot3starter.common.security.models.TokenType;
import vn.eztek.springboot3starter.common.security.services.UserDetailsServiceImpl;

// filter value of Authorization header
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
  private final Set<String> allowedUrls = new HashSet<>(
      List.of(
          "/api/auth/**",
          "/websocket/**",
          "/public-access/**"
      ));
  @Autowired
  private JwtProvider tokenProvider;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;


  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
      throws ServletException, IOException {
    var allowed = allowedUrls.stream()
        .anyMatch(url -> new AntPathRequestMatcher(url, null, false).matches(request));
    if (allowed) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      var jwt = getJwt(request);
      if (StringUtils.hasText(jwt) && tokenProvider.validateJwtToken(jwt) && tokenProvider
          .getTokenType(jwt).equals(TokenType.ACCESS_TOKEN)) {
        var username = tokenProvider.getUserNameFromJwtToken(jwt);
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e.getMessage());
      throw new UnauthorizedException("invalid-token");
    }

    filterChain.doFilter(request, response);
  }

  private String getJwt(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");
    if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
      return null;
    }
    return bearerToken.substring(7);
  }

}
