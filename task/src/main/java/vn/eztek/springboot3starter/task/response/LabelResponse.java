package vn.eztek.springboot3starter.task.response;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

import java.util.UUID;

@Getter
@Setter
public class LabelResponse implements CommandResult, QueryResult {
  private UUID id;

  private String name;

  private UUID taskId;

  private String color;

  private Boolean isMarked;
}
