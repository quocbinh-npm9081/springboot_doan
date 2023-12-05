package vn.eztek.springboot3starter.project.command.validated;

import lombok.Value;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class UpdateProjectCommandValidated implements CommandValidated {

  Project project;

}
