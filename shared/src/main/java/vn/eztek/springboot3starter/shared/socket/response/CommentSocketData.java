package vn.eztek.springboot3starter.shared.socket.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Setter
@Getter
@AllArgsConstructor
public class CommentSocketData implements SocketResponseData {

  private UUID id;
  private UserSocketData user;
  private UUID taskId;
  private String content;
  private UUID parentId;
  private Integer countReply;
  private ZonedDateTime createdDate;
  private ZonedDateTime lastModifiedDate;

}
