package vn.eztek.springboot3starter.project.command;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UpdateStatusMemberInProjectCommand implements Command {

  UUID projectId;
  UUID memberId;
  UserProjectStatus status;

}
