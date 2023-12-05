package vn.eztek.springboot3starter.shared.socket.response;

import java.util.List;
import lombok.Getter;
import lombok.Value;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Value(staticConstructor = "create")
public class UpdateStageSocketData implements SocketResponseData {

  List<StageSocketData> stages;
}
