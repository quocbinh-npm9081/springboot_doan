package vn.eztek.springboot3starter.task.controller;

import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.aggregate.TaskAggregate;
import vn.eztek.springboot3starter.task.command.AssignTaskCommand;
import vn.eztek.springboot3starter.task.command.BatchUpdateTaskCommand;
import vn.eztek.springboot3starter.task.command.CreateTaskCommand;
import vn.eztek.springboot3starter.task.command.DeleteTaskCommand;
import vn.eztek.springboot3starter.task.command.UnassignTaskCommand;
import vn.eztek.springboot3starter.task.command.UpdateTaskCommand;
import vn.eztek.springboot3starter.task.query.GetTaskByIdQuery;
import vn.eztek.springboot3starter.task.query.ListTasksQuery;
import vn.eztek.springboot3starter.task.request.AssignTaskRequest;
import vn.eztek.springboot3starter.task.request.BatchUpdateTaskRequest;
import vn.eztek.springboot3starter.task.request.CreateTaskRequest;
import vn.eztek.springboot3starter.task.response.SimplifyTaskResponse;
import vn.eztek.springboot3starter.task.response.TaskResponse;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final ApplicationContext applicationContext;

  @PostMapping
  @PreAuthorize("hasRole('CREATE_TASK')")
  public ResponseEntity<List<TaskResponse>> createTask(
      @Valid @RequestBody CreateTaskRequest request) {

    var command = CreateTaskCommand.commandOf(request.getTitle(), request.getDescription(),
        request.getProjectId(), request.getStageId(), request.getPreviousId(), request.getNextId());

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<TaskResponse> tasks = aggregate.handle(command);

    return ResponseEntity.ok(tasks.getContent());

  }

  @PatchMapping(value = "/{id}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id,
      @RequestBody JsonPatch request) {
    var command = UpdateTaskCommand.commandOf(id, request);

    var aggregate = new TaskAggregate(applicationContext);
    TaskResponse task = aggregate.handle(command);

    return ResponseEntity.ok(task);
  }

  @PostMapping(value = "/{id}/assign")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<TaskResponse> assignTaskToUser(@PathVariable UUID id,
      @Valid @RequestBody AssignTaskRequest request) {
    var command = AssignTaskCommand.commandOf(id, request.getUserId());

    var aggregate = new TaskAggregate(applicationContext);
    TaskResponse task = aggregate.handle(command);

    return ResponseEntity.ok(task);
  }

  @PostMapping(value = "/{id}/unassign")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<TaskResponse> unassignedTaskToUser(@PathVariable UUID id) {
    var command = UnassignTaskCommand.commandOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    TaskResponse task = aggregate.handle(command);

    return ResponseEntity.ok(task);
  }

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VIEW_TASK')")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
    var query = GetTaskByIdQuery.queryOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    TaskResponse task = aggregate.handle(query);

    return ResponseEntity.ok(task);
  }

  @GetMapping()
  @PreAuthorize("hasRole('VIEW_TASK')")
  public ResponseEntity<List<SimplifyTaskResponse>> getTasksInProject(
      @RequestParam("projectId") UUID id) {
    var query = ListTasksQuery.queryOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<SimplifyTaskResponse> tasks = aggregate.handle(query);

    return ResponseEntity.ok(tasks.getContent());
  }

  @PostMapping("/batch-update")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<List<TaskResponse>> batchUpdateTask(
      @Valid @RequestBody List<BatchUpdateTaskRequest> request) {
    var command = BatchUpdateTaskCommand.commandOf(request);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<TaskResponse> tasks = aggregate.handle(command);

    return ResponseEntity.ok(tasks.getContent());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('DELETE_TASK')")
  public ResponseEntity<List<TaskResponse>> deleteTask(@PathVariable UUID id) {
    var command = DeleteTaskCommand.commandOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<TaskResponse> tasks = aggregate.handle(command);

    return ResponseEntity.ok(tasks.getContent());
  }

}
