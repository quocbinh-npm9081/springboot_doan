package vn.eztek.springboot3starter.invitation.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class InvitationAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public InvitationAggregateId() {
    super();
  }

  public InvitationAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "INVITATION-%s";
  }
}
