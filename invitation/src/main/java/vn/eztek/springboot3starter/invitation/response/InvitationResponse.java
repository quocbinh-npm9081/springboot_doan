package vn.eztek.springboot3starter.invitation.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class InvitationResponse implements QueryResult {

  UUID id;
  Boolean used;
  String projectName;
  InviterResponse inviter;
  private ZonedDateTime expiredTime;
  private String key;

}
