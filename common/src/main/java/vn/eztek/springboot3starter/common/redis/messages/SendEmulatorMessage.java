package vn.eztek.springboot3starter.common.redis.messages;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmulatorMessage {

  List<SendEmulator> messages;

  public static SendEmulatorMessage create(List<SendEmulator> messages) {
    return new SendEmulatorMessage(messages);
  }
}
