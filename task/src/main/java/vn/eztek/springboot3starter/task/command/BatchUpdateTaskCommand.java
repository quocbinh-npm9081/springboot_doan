package vn.eztek.springboot3starter.task.command;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Command;
import vn.eztek.springboot3starter.task.request.BatchUpdateTaskRequest;

@Value(staticConstructor = "commandOf")
public class BatchUpdateTaskCommand implements Command {

  List<@Valid BatchUpdateTaskRequest> tasks;

}
