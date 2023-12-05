package vn.eztek.springboot3starter.shared.emulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyEmulatorData {

  String url;
  String email;
  String type;
  String key;

  public static KeyEmulatorData create(String url, String email, String type, String key) {
    return new KeyEmulatorData(url, email, type, key);
  }
}
