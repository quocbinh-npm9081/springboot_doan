package vn.eztek.springboot3starter.publicAccess.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class InvitationViaLinkResponse implements CommandResult {

  UUID projectId;

  public InvitationViaLinkResponse(UUID projectId) {

    this.projectId = projectId;
  }
}
