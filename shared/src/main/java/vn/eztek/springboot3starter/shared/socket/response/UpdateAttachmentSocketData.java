package vn.eztek.springboot3starter.shared.socket.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class UpdateAttachmentSocketData implements SocketResponseData {

  private UUID id;
  private String originalName;

  public static UpdateAttachmentSocketData create(UUID id, String originalName) {
    return new UpdateAttachmentSocketData(id, originalName);
  }
}
