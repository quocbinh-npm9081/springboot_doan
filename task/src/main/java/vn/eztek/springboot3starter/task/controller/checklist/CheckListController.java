package vn.eztek.springboot3starter.task.controller.checklist;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.task.aggregate.TaskAggregate;
import vn.eztek.springboot3starter.task.command.checklist.CreateCheckListCommand;
import vn.eztek.springboot3starter.task.command.checklist.DeleteCheckListCommand;
import vn.eztek.springboot3starter.task.command.checklist.UpdateCheckListCommand;
import vn.eztek.springboot3starter.task.query.checklist.GetCheckListByTaskIdQuery;
import vn.eztek.springboot3starter.task.request.checklist.CreateCheckListRequest;
import vn.eztek.springboot3starter.task.request.checklist.UpdateCheckListRequest;
import vn.eztek.springboot3starter.task.response.CheckListResponse;

@RestController
@RequestMapping("/api/tasks/{id}/check-list")
@RequiredArgsConstructor
public class CheckListController {

  private final ApplicationContext applicationContext;

  @PostMapping
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListResponse> createCheckList(@PathVariable UUID id,
      @Valid @RequestBody CreateCheckListRequest request) {

    var command = CreateCheckListCommand.commandOf(id, request.getName());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/{checkListId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListResponse> updateCheckList(@PathVariable UUID id,
      @PathVariable UUID checkListId, @Valid @RequestBody UpdateCheckListRequest request) {

    var command = UpdateCheckListCommand.commandOf(id, checkListId, request.getName());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @DeleteMapping(value = "/{checkListId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<Void> deleteCheckList(@PathVariable UUID id,
      @PathVariable UUID checkListId) {

    var command = DeleteCheckListCommand.commandOf(id, checkListId);

    var aggregate = new TaskAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @GetMapping
  @PreAuthorize("hasRole('VIEW_TASK')")
  public ResponseEntity<List<CheckListResponse>> getCheckListInTask(@PathVariable UUID id) {

    var query = GetCheckListByTaskIdQuery.queryOf(id);

    var aggregate = new TaskAggregate(applicationContext);
    ListResponse<CheckListResponse> response = aggregate.handle(query);

    return ResponseEntity.ok(response.getContent());
  }
}
