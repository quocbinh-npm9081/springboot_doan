package vn.eztek.springboot3starter.project.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class StageResponse implements CommandResult {

  UUID id;
  String name;

}
