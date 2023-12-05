package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmulator {

  String url;
  String email;
  String type;
  String key;

  public static SendEmulator create(String url, String email, String type, String key) {
    return new SendEmulator(url, email, type, key);
  }
}
