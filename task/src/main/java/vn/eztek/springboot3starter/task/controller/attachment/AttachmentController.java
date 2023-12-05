package vn.eztek.springboot3starter.task.controller.attachment;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.task.aggregate.TaskAggregate;
import vn.eztek.springboot3starter.task.command.attachmnet.DeleteAttachmentCommand;
import vn.eztek.springboot3starter.task.command.attachmnet.UpdateAttachmentCommand;
import vn.eztek.springboot3starter.task.command.attachmnet.UploadAttachmentCommand;
import vn.eztek.springboot3starter.task.request.attachment.UpdateAttachmentRequest;
import vn.eztek.springboot3starter.task.response.AttachmentResponse;

@RestController
@RequestMapping("/api/tasks/{id}")
@RequiredArgsConstructor
public class AttachmentController {

  private final ApplicationContext applicationContext;

  @PostMapping(value = "/attachments", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<AttachmentResponse> uploadAttachment(@PathVariable UUID id,
      @RequestPart("file") MultipartFile file) {
    var command = UploadAttachmentCommand.commandOf(id, file);

    var aggregate = new TaskAggregate(applicationContext);
    AttachmentResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @PutMapping(value = "/attachments/{attachmentId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<AttachmentResponse> updateAttachment(@PathVariable UUID id,
      @PathVariable UUID attachmentId, @Valid @RequestBody UpdateAttachmentRequest request) {
    var command = UpdateAttachmentCommand.commandOf(id, attachmentId, request.getName());

    var aggregate = new TaskAggregate(applicationContext);
    AttachmentResponse attachmentResponse = aggregate.handle(command);

    return ResponseEntity.ok(attachmentResponse);
  }

  @DeleteMapping(value = "/attachments/{attachmentId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<Void> deleteAttachmentInTask(@PathVariable UUID id,
      @PathVariable UUID attachmentId) {
    var command = DeleteAttachmentCommand.commandOf(id, attachmentId);

    var aggregate = new TaskAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

}
