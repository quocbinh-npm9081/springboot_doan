package vn.eztek.springboot3starter.shared.redis;


import lombok.Getter;
import lombok.Setter;
import lombok.Value;


@Getter
@Setter
@Value(staticConstructor = "create")
public class MessageEvent {

  MessageEventType type;
  String message;
}
