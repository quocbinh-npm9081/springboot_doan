package vn.eztek.springboot3starter.profile.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class ProfileAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public ProfileAggregateId() {
    super();
  }

  public ProfileAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "PROFILE-%s";
  }

}
