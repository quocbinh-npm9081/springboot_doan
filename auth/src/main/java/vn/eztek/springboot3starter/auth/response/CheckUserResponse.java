package vn.eztek.springboot3starter.auth.response;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class CheckUserResponse implements CommandResult {

  Boolean isRegisteringUser;
}
