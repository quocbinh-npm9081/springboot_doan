package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinishVerifyAccountMessage {

  String userId;

  public static FinishVerifyAccountMessage create(String userId) {
    return new FinishVerifyAccountMessage(userId);
  }
}
