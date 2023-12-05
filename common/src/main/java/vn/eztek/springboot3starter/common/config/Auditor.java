package vn.eztek.springboot3starter.common.config;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auditor implements AuditorAware<String> {

  private static final String DEFAULT_USER = "system";

  @Override
  public @NotNull Optional<String> getCurrentAuditor() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return Optional.of(DEFAULT_USER);
    }

    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
  }

}
