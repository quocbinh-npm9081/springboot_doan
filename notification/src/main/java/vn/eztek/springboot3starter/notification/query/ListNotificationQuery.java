package vn.eztek.springboot3starter.notification.query;


import lombok.Value;
import org.springframework.data.domain.Pageable;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class ListNotificationQuery implements Query {

  Pageable pageable;
}
