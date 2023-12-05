package vn.eztek.springboot3starter.task.controller.checklistitem;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.task.aggregate.TaskAggregate;
import vn.eztek.springboot3starter.task.command.checklistitem.AssignCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.CreateCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.DeleteCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.UnAssignCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateCheckListItemCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateDueDateCommand;
import vn.eztek.springboot3starter.task.command.checklistitem.UpdateStatusCheckListItemCommand;
import vn.eztek.springboot3starter.task.request.checklistitem.AssignCheckListItemRequest;
import vn.eztek.springboot3starter.task.request.checklistitem.CreateCheckListItemRequest;
import vn.eztek.springboot3starter.task.request.checklistitem.UpdateCheckListItemRequest;
import vn.eztek.springboot3starter.task.request.checklistitem.UpdateDueDateRequest;
import vn.eztek.springboot3starter.task.request.checklistitem.UpdateStatusCheckListItemRequest;
import vn.eztek.springboot3starter.task.response.CheckListItemResponse;

@RestController
@RequestMapping("/api/tasks/{id}/check-list/{checkListId}/check-list-item")
@RequiredArgsConstructor
public class CheckListItemController {

  private final ApplicationContext applicationContext;

  @PostMapping
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> createCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @Valid @RequestBody CreateCheckListItemRequest request) {

    var command = CreateCheckListItemCommand.commandOf(id, checkListId, request.getContent());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/{checkListItemId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> updateCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId,
      @Valid @RequestBody UpdateCheckListItemRequest request) {

    var command = UpdateCheckListItemCommand.commandOf(id, checkListId, checkListItemId,
        request.getContent());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @DeleteMapping(value = "/{checkListItemId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<Void> deleteCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId) {

    var command = DeleteCheckListItemCommand.commandOf(id, checkListId, checkListItemId);

    var aggregate = new TaskAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PutMapping(value = "/{checkListItemId}/assign")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> assignCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId,
      @Valid @RequestBody AssignCheckListItemRequest request) {

    var command = AssignCheckListItemCommand.commandOf(id, checkListId, checkListItemId,
        request.getUserId());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/{checkListItemId}/un-assign")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> unAssignCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId) {

    var command = UnAssignCheckListItemCommand.commandOf(id, checkListId, checkListItemId);

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/{checkListItemId}/status")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> updateStatusCheckListItem(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId,
      @Valid @RequestBody UpdateStatusCheckListItemRequest request) {

    var command = UpdateStatusCheckListItemCommand.commandOf(id, checkListId, checkListItemId,
        request.getStatus());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/{checkListItemId}/due-date")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CheckListItemResponse> updateDueDate(@PathVariable UUID id,
      @PathVariable UUID checkListId, @PathVariable UUID checkListItemId,
      @Valid @RequestBody UpdateDueDateRequest request) {

    var command = UpdateDueDateCommand.commandOf(id, checkListId, checkListItemId,
        request.getDueDate());

    var aggregate = new TaskAggregate(applicationContext);
    CheckListItemResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

}
