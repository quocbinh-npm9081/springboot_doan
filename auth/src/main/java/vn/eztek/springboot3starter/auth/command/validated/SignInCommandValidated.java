package vn.eztek.springboot3starter.auth.command.validated;

import lombok.Value;
import org.springframework.security.core.Authentication;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class SignInCommandValidated implements CommandValidated {

  Authentication authentication;

}
