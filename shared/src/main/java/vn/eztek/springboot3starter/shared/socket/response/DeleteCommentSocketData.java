package vn.eztek.springboot3starter.shared.socket.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Setter
@Getter
@Value(staticConstructor = "create")
public class DeleteCommentSocketData implements SocketResponseData {

  String id;

}
