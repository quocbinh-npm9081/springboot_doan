package vn.eztek.springboot3starter.task.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;

@Getter
@Setter
public class CommentResponse implements CommandResult, QueryResult, SocketResponseData {

  private UUID id;
  private UserResponse user;
  private UUID taskId;
  private String content;
  private UUID parentCommentId;
  private Integer countReply;
  private ZonedDateTime createdDate;
  private ZonedDateTime lastModifiedDate;

}
