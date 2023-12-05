package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Getter
@Setter
@AllArgsConstructor
public class DeleteAttachmentSocketData implements SocketResponseData {

  String id;

  public static DeleteAttachmentSocketData create(String id) {
    return new DeleteAttachmentSocketData(id);
  }

}
