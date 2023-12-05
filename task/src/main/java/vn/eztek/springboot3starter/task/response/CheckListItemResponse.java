package vn.eztek.springboot3starter.task.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class CheckListItemResponse implements CommandResult {

  private UUID id;

  private String content;

  private UUID checkListId;

  private ZonedDateTime dueDate;

  private UserResponse assignee;

  private Boolean isDone;

}
