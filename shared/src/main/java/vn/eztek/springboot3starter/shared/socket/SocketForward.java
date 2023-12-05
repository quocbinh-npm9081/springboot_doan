package vn.eztek.springboot3starter.shared.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketForward {

  SocketResponseType type;
  String data;

  public static SocketForward create(SocketResponseType type, String data) {
    return new SocketForward(type, data);
  }

}
