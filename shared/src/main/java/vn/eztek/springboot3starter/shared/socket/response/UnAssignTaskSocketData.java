package vn.eztek.springboot3starter.shared.socket.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
@Value(staticConstructor = "create")
public class UnAssignTaskSocketData implements SocketResponseData {

  String id;

}
