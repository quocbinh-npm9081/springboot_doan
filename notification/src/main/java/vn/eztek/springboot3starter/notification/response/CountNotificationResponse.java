package vn.eztek.springboot3starter.notification.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
@Value(staticConstructor = "countOf")
public class CountNotificationResponse implements CommandResult, QueryResult {
  Long count;
}
