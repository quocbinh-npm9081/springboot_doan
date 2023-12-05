package vn.eztek.springboot3starter.task.controller.label;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.aggregate.TaskAggregate;
import vn.eztek.springboot3starter.task.command.label.*;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdAndIsMarkedTrueQuery;
import vn.eztek.springboot3starter.task.query.label.GetLabelByTaskIdQuery;
import vn.eztek.springboot3starter.task.request.label.AssignLabelInTaskRequest;
import vn.eztek.springboot3starter.task.request.label.CreateLabelRequest;
import vn.eztek.springboot3starter.task.request.label.UnAssignLabelInTaskRequest;
import vn.eztek.springboot3starter.task.request.label.UpdateLabelRequest;
import vn.eztek.springboot3starter.task.response.LabelResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{id}/labels")
public class LabelController {
  private final ApplicationContext applicationContext;

  @PostMapping
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<LabelResponse> createLabel(@PathVariable UUID id,
                                                   @Valid @RequestBody CreateLabelRequest request) {
    var command = CreateLabelCommand.commandOf(id, request.getName(), request.getColor());

    var aggregate = new TaskAggregate(applicationContext);
    LabelResponse labelResponse = aggregate.handle(command);

    return ResponseEntity.ok(labelResponse);
  }

  @PutMapping("/{labelId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<LabelResponse> updateLabel(@PathVariable UUID id, @PathVariable UUID labelId,
                                                   @Valid @RequestBody UpdateLabelRequest request) {
    var command = UpdateLabelCommand.commandOf(id, labelId, request.getName(), request.getColor());

    var aggregate = new TaskAggregate(applicationContext);
    LabelResponse labelResponse = aggregate.handle(command);

    return ResponseEntity.ok(labelResponse);
  }

  @DeleteMapping("/{labelId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<Void> deleteLabel(@PathVariable UUID id, @PathVariable UUID labelId) {
    var command = DeleteLabelCommand.commandOf(id, labelId);

    var aggregate = new TaskAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @GetMapping
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<ListResponse<LabelResponse>> getListLabels(@PathVariable UUID id) {
    var query = GetLabelByTaskIdQuery.queryOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<LabelResponse> labelResponse = aggregate.handle(query);

    return ResponseEntity.ok(labelResponse);
  }

  @GetMapping("/is-marked")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<ListResponse<LabelResponse>> getListLabelsIsMarkedTrue(@PathVariable UUID id) {
    var query = GetLabelByTaskIdAndIsMarkedTrueQuery.queryOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<LabelResponse> labelResponse = aggregate.handle(query);

    return ResponseEntity.ok(labelResponse);
  }

  @PostMapping("/assign-label-in-task")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<ListResponse<LabelResponse>> assignLabelInTask(@PathVariable UUID id, @Valid @RequestBody AssignLabelInTaskRequest request) {
    var command = AssignLabelInTaskCommand.commandOf(id, request.getLabelIds());

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<LabelResponse> labelResponse = aggregate.handle(command);

    return ResponseEntity.ok(labelResponse);
  }

  @PostMapping("/remove-label-in-task")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<ListResponse<LabelResponse>> removeLabelInTask(@PathVariable UUID id, @Valid @RequestBody UnAssignLabelInTaskRequest request) {
    var command = UnAssignLabelInTaskCommand.commandOf(id, request.getLabelIds());

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<LabelResponse> labelResponse = aggregate.handle(command);

    return ResponseEntity.ok(labelResponse);
  }

  @PostMapping("{labelId}/remove-color")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<LabelResponse> removeColor(@PathVariable UUID id, @PathVariable UUID labelId) {
    var command = RemoveLabelColorCommand.commandOf(id, labelId);

    var aggregate = new TaskAggregate(applicationContext);
    LabelResponse labelResponse = aggregate.handle(command);

    return ResponseEntity.ok(labelResponse);
  }
}
