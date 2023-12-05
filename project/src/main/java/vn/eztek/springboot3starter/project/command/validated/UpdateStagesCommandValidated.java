package vn.eztek.springboot3starter.project.command.validated;

import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class UpdateStagesCommandValidated implements CommandValidated {

  List<Stage> stagesPreUpdate;
  List<Stage> deleteStages;
  User user;
  Project project;

}
