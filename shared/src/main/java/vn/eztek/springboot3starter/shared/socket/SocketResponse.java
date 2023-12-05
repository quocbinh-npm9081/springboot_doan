package vn.eztek.springboot3starter.shared.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketResponse {

  SocketResponseType type;
  SocketResponseData data;

  public static SocketResponse create(SocketResponseType type, SocketResponseData data) {
    return new SocketResponse(type, data);
  }

}
