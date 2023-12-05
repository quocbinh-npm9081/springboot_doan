package vn.eztek.springboot3starter.user.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class UserAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public UserAggregateId() {
    super();
  }

  public UserAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "USER-%s";
  }

}
