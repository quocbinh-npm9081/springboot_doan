package vn.eztek.springboot3starter.notification.vo;

import java.io.Serial;
import vn.eztek.springboot3starter.shared.cqrs.RandomUUID;

public class NotificationAggregateId extends RandomUUID {

  @Serial
  private static final long serialVersionUID = 1L;

  public NotificationAggregateId() {
    super();
  }

  public NotificationAggregateId(String id) {
    super(id);
  }

  @Override
  protected String getPrefix() {
    return "NOTIFICATION-%s";
  }

}
