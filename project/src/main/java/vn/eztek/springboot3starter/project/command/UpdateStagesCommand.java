package vn.eztek.springboot3starter.project.command;

import java.util.List;
import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.project.request.StageRequest;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateStagesCommand implements Command {

  UUID projectId;
  List<StageRequest> stageList;

}
