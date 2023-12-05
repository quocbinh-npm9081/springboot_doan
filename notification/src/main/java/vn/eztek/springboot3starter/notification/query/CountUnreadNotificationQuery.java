package vn.eztek.springboot3starter.notification.query;


import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class CountUnreadNotificationQuery implements Query {
}
