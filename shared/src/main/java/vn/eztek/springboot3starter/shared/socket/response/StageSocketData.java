package vn.eztek.springboot3starter.shared.socket.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StageSocketData implements SocketResponseData {

  UUID id;
  String name;

}
