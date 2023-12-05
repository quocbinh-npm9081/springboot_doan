package vn.eztek.springboot3starter.task.request.checklistitem;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDueDateRequest {

  ZonedDateTime dueDate;
}
