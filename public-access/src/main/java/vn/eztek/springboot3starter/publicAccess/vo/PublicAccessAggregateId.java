package vn.eztek.springboot3starter.publicAccess.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class PublicAccessAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public PublicAccessAggregateId() {
    super();
  }

  public PublicAccessAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "PUBLIC-ACCESS-%s";
  }
}
