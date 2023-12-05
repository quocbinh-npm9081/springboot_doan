package vn.eztek.springboot3starter.auth.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class AuthAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public AuthAggregateId() {
    super();
  }

  public AuthAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "AUTH-%s";
  }

}
