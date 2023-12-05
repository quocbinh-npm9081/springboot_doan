package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAccountMessage {

  String firstName;
  String lastName;
  String username;
  String userId;
  String keyRandom;

  public static VerifyAccountMessage create(String firstName, String lastName, String username,
      String userId, String keyRandom) {
    return new VerifyAccountMessage(firstName, lastName, username, userId, keyRandom);
  }
}
