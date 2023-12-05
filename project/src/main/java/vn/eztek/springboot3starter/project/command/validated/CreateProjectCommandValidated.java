package vn.eztek.springboot3starter.project.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class CreateProjectCommandValidated implements CommandValidated {

  User user;

}
