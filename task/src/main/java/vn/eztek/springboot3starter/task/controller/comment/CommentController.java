package vn.eztek.springboot3starter.task.controller.comment;

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
import vn.eztek.springboot3starter.task.command.comment.DeleteCommentCommand;
import vn.eztek.springboot3starter.task.command.comment.PostCommentCommand;
import vn.eztek.springboot3starter.task.command.comment.UpdateCommentCommand;
import vn.eztek.springboot3starter.task.query.GetChildrenCommentQuery;
import vn.eztek.springboot3starter.task.query.GetCommentByTaskIdQuery;
import vn.eztek.springboot3starter.task.request.comment.PostCommentRequest;
import vn.eztek.springboot3starter.task.request.comment.UpdateCommentRequest;
import vn.eztek.springboot3starter.task.response.CommentResponse;

@RestController
@RequestMapping("/api/tasks/{id}")
@RequiredArgsConstructor
public class CommentController {

  private final ApplicationContext applicationContext;

  @PostMapping(value = "/comments")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CommentResponse> uploadComment(@PathVariable UUID id,
      @Valid @RequestBody PostCommentRequest request) {

    var command = PostCommentCommand.commandOf(id, request.getContent(), request.getParentId());

    var aggregate = new TaskAggregate(applicationContext);
    CommentResponse commentResponse = aggregate.handle(command);

    return ResponseEntity.ok(commentResponse);
  }

  @GetMapping(value = "/comments")
  @PreAuthorize("hasRole('VIEW_TASK')")
  public ResponseEntity<List<CommentResponse>> getCommentInTask(@PathVariable UUID id) {

    var query = GetCommentByTaskIdQuery.queryOf(id);
    var aggregate = new TaskAggregate(applicationContext);

    ListResponse<CommentResponse> commentResponses = aggregate.handle(query);

    return ResponseEntity.ok(commentResponses.getContent());
  }

  @GetMapping(value = "/comments/{commentId}/replies")
  @PreAuthorize("hasRole('VIEW_TASK')")
  public ResponseEntity<List<CommentResponse>> getChildrenComment(@PathVariable UUID id,
      @PathVariable UUID commentId) {

    var query = GetChildrenCommentQuery.queryOf(id, commentId);
    var aggregate = new TaskAggregate(applicationContext);

    ListResponse<CommentResponse> commentResponses = aggregate.handle(query);

    return ResponseEntity.ok(commentResponses.getContent());
  }

  @PutMapping(value = "/comments/{commentId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<CommentResponse> updateComment(@PathVariable UUID id,
      @PathVariable UUID commentId, @Valid @RequestBody UpdateCommentRequest request) {

    var command = UpdateCommentCommand.commandOf(id, commentId, request.getContent());

    var aggregate = new TaskAggregate(applicationContext);
    CommentResponse commentResponse = aggregate.handle(command);

    return ResponseEntity.ok(commentResponse);
  }

  @DeleteMapping(value = "/comments/{commentId}")
  @PreAuthorize("hasRole('UPDATE_TASK')")
  public ResponseEntity<Void> deleteCommentById(@PathVariable UUID id,
      @PathVariable UUID commentId) {
    var command = DeleteCommentCommand.commandOf(id, commentId);

    var aggregate = new TaskAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

}
