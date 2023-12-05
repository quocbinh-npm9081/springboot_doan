package vn.eztek.springboot3starter.project.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class ProjectAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public ProjectAggregateId() {
    super();
  }

  public ProjectAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "PROJECT-%s";
  }
}
