package vn.eztek.springboot3starter.auth.command.validated;

import lombok.Value;
import org.springframework.security.core.Authentication;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class RefreshTokenCommandValidated implements CommandValidated {

  Authentication authentication;
}
