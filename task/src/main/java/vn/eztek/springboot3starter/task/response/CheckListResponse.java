package vn.eztek.springboot3starter.task.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class CheckListResponse implements CommandResult, QueryResult {

  private UUID id;

  private String name;

  private UUID taskId;

  List<CheckListItemResponse> checkListItems;
}
