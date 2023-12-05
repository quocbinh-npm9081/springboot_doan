package vn.eztek.springboot3starter.task.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class TaskAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public TaskAggregateId() {
    super();
  }

  public TaskAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "TASK-%s";
  }
}
