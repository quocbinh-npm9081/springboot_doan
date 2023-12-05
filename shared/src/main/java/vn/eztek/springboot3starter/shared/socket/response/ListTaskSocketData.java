package vn.eztek.springboot3starter.shared.socket.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@AllArgsConstructor
public class ListTaskSocketData implements SocketResponseData {

  List<TaskSocketData> tasks;
}
