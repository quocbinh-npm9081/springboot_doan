package vn.eztek.springboot3starter.shared.socket.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class AttachmentSocketData implements SocketResponseData {

  private UUID id;
  private String name;
  private String originalName;
  private UUID taskId;

}
